package com.ferfox1981.springbatchrest.integration.covidcenter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ferfox1981.springbatchrest.adapter.LocalDateTimeAdapter;
import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.entity.Identity;
import com.ferfox1981.springbatchrest.entity.Measurements;
import com.ferfox1981.springbatchrest.util.Messages;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class ExternalConsumer {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Identity identity;
	
	@Autowired
	private Messages messages;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Identity getIdentity(
			@Value("${firebase.email}") String email,
			@Value("${firebase.password}") String password,
			@Value("${firebase.API_KEY}") String API_KEY
			) throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject jO = new JsonObject();
		jO.add("email", new JsonPrimitive(email));
		jO.add("password", new JsonPrimitive(password));
		jO.add("returnSecureToken", new JsonPrimitive("true"));
		
		 HttpEntity<String> request = 
			      new HttpEntity<String>(jO.toString(), headers);
		
		ResponseEntity<Identity> response = restTemplate.exchange(
				"https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="+API_KEY,
		        HttpMethod.POST,
		        request,
		        Identity.class
		);
		System.out.println(response);
		
		return response.getBody();
		
	}


	public String getFireBaseData() throws Exception {
		String test = restTemplate.getForObject("https://covid-moving-average.firebaseio.com/covid.json?auth="+identity.getIdToken(), String.class);
		
		return test;
	}
	
	public void saveToFireBase(Measurements measurements) {
				 
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
		String s = gson.toJson(measurements);
		
		restTemplate.put("https://covid-moving-average.firebaseio.com/covid.json?auth="+identity.getIdToken(), s, String.class);
		

		
	}

	
	public List<CovidData> getDaysData() throws Exception {
		
		List<CovidData> lista = new ArrayList<CovidData>();
		List<String> dias = getLastDays();
		for (String string : dias) {
			String json = restTemplate.getForObject("https://covid19-brazil-api.now.sh/api/report/v1/brazil/" + string,
					String.class);
			JSONObject jsonObject = new JSONObject(json);

			String array = jsonObject.getString("data");
			JSONArray jsonArray = new JSONArray(array);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonEstrutura = jsonArray.getJSONObject(i);
				if (jsonEstrutura.has("uf") && jsonEstrutura.getString("uf").compareTo("PE") == 0) {
					CovidData cd = new CovidData();
					cd.setCases(new Integer(jsonEstrutura.getString("cases")));
					cd.setDeaths(new Integer(jsonEstrutura.getString("deaths")));
					cd.setCases(new Integer(jsonEstrutura.getString("cases")));
					cd.setState(jsonEstrutura.getString("state"));

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
							Locale.ENGLISH);
					LocalDateTime date = LocalDateTime.parse(jsonEstrutura.getString("datetime"), formatter);
					cd.setDate(date);
					lista.add(cd);
				}
			}

		}

		return lista;
	}

	public List<String> getLastDays() {

		// create array with last seven days
		Calendar calendar = Calendar.getInstance();
		List<String> lista = new ArrayList<String>();
		for (int i = 1; i <= Integer.parseInt(messages.COVID_DAYS); i++) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			lista.add(calendar.get(Calendar.YEAR) + Strings.padStart(calendar.get(Calendar.MONTH)+1 + "", 2, '0')
					+ Strings.padStart(calendar.get(Calendar.DAY_OF_MONTH) + "", 2, '0'));
		}

		return lista;

	}

}
