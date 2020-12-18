package com.ferfox1981.springbatchrest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Messages {
	
	@Value("${covid.days}")
	public String COVID_DAYS;
	
	@Value("${firebase.email}")
	public String EMAIL;
	

	
	@Value("${firebase.password}")
	public String PASSWORD;
	
	@Value("${firebase.API_KEY}")
	public String API_KEY;
	

}
