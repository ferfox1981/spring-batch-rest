package com.ferfox1981.springbatchrest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Measurements implements Serializable{

	
	public List<CovidData> covidMeasurements;
	
	public List<CovidData> getCovidMeasurements() {
		return covidMeasurements;
	}

	public void setCovidMeasurements(List<CovidData> covidMeasurements) {
		this.covidMeasurements = covidMeasurements;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Measurements() {
		this.covidMeasurements = new ArrayList<CovidData>();
	}
	
	
	
	

}
