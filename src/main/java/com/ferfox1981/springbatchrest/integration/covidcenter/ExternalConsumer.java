package com.ferfox1981.springbatchrest.integration.covidcenter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ferfox1981.springbatchrest.entity.CovidData;
import com.google.common.base.Strings;

@Component
public class ExternalConsumer {
	
	@Autowired
	private RestTemplate restTemplate;
	
    @Bean
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }	
    
    public List<CovidData> getData(String cnpj) throws Exception {
    	

    	List<String> dias = getSeteUltimosDias();
    	
    	String json = restTemplate.getForObject("https://covid19-brazil-api.now.sh/api/report/v1/brazil/20200318", String.class);
    	JSONObject jsonObject = new JSONObject(json);
    	
    	String array = jsonObject.getString("data");
    	JSONArray jsonArray = new JSONArray(array);
  
    	    	for(int i =0 ;i < jsonArray.length();i++) {
    	    		JSONObject jsonEstrutura = jsonArray.getJSONObject(i);
    	    		if (jsonEstrutura.has("uf") && jsonEstrutura.getString("uf").compareTo("PE") == 0) {
    	    			CovidData cd = new CovidData();
    	    			cd.setCases(new Integer(jsonEstrutura.getString("cases")));
    	    			cd.setDeaths(new Integer(jsonEstrutura.getString("deaths")));
    	    			cd.setCases(new Integer(jsonEstrutura.getString("cases")));
    	    			cd.setState(jsonEstrutura.getString("state"));
    	    			
    	    			DateTimeFormatter formatter = 
    	    			        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    	    			LocalDateTime date = LocalDateTime.parse(jsonEstrutura.getString("datetime"), formatter);
    	    			cd.setDate(date);
    	    			
    	    		}
    	    	}
    	    	
    	    
    	
    	

    	return null;
    }
    
    public List<String> getSeteUltimosDias(){
    	
    	// criar array com os últimos 7 dias
    	Calendar calendar = Calendar.getInstance();
    	List<String> lista = new ArrayList<String>();
    	for(int i = 1; i <= 7; i++) {
	    	calendar.add(Calendar.DATE, -1);
	    	lista.add(calendar.get(Calendar.YEAR)+Strings.padStart(calendar.get(Calendar.MONTH)+"", 2,'0')+Strings.padStart(calendar.get(Calendar.DAY_OF_MONTH)+"", 2,'0'));
    	}
    	
    	return lista;
    	
    }

}
