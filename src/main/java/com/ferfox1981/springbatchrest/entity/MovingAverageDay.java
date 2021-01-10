package com.ferfox1981.springbatchrest.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovingAverageDay {
	
	public Integer getDeaths() {
		return deaths;
	}
	public void setDeaths(Integer deaths) {
		this.deaths = deaths;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	private Integer deaths;
	private LocalDateTime date;
	private String movAvgDay;
	
	public String getMovAvgDay() {
		return movAvgDay;
	}
	public void setMovAvgDay(String movAvgDay) {
		this.movAvgDay = movAvgDay;
	}

}
