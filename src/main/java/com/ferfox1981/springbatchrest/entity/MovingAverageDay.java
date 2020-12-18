package com.ferfox1981.springbatchrest.entity;

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
	private Float movAvgDay;
	
	public Float getMovAvgDay() {
		return movAvgDay;
	}
	public void setMovAvgDay(Float movAvgDay) {
		this.movAvgDay = movAvgDay;
	}

}
