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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ferfox1981.springbatchrest.adapter.LocalDateTimeAdapter;
import com.ferfox1981.springbatchrest.entity.CovidData;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class ExternalConsumer {

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public String getFireBaseData() throws Exception {
		String test = restTemplate.getForObject("https://covid-moving-average.firebaseio.com/covid.json", String.class);
		
		return test;
	}
	
	public void saveToFireBase(List<CovidData> list) {
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
		String s = gson.toJson(list);
		restTemplate.postForObject("https://covid-moving-average.firebaseio.com/covid.json", s, String.class);
	}
	
	public List<CovidData> getDaysData() throws Exception {
		
		List<CovidData> lista = new ArrayList<CovidData>();
		List<String> dias = getSevenLastDays();
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

	public List<String> getSevenLastDays() {

		// create array with last seven days
		Calendar calendar = Calendar.getInstance();
		List<String> lista = new ArrayList<String>();
		for (int i = 1; i <= 7; i++) {
			calendar.add(Calendar.DATE, -1);
			lista.add(calendar.get(Calendar.YEAR) + Strings.padStart(calendar.get(Calendar.MONTH) + "", 2, '0')
					+ Strings.padStart(calendar.get(Calendar.DAY_OF_MONTH) + "", 2, '0'));
		}

		return lista;

	}

}
