package com.ferfox1981.springbatchrest.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidData implements Serializable{


	public CovidData(LocalDateTime date, String state, Integer cases, Integer deaths) {
		super();
		this.date = date;
		this.state = state;
		this.cases = cases;
		this.deaths = deaths;
	}
	public CovidData() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getCases() {
		return cases;
	}
	public void setCases(Integer cases) {
		this.cases = cases;
	}
	public Integer getDeaths() {
		return deaths;
	}
	public void setDeaths(Integer deaths) {
		this.deaths = deaths;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
	public LocalDateTime date;
    public String state;
    public Integer cases;
    public Integer deaths;    
    
    @Override
    public boolean equals(Object obj) {
    	if(this == obj) 
            return true;
    	if(obj == null || obj.getClass()!= this.getClass()) 
            return false;
    	
    	CovidData c = (CovidData) obj;
    	
    	return (this.cases.intValue() == c.cases.intValue() 
    			&& this.deaths.intValue() == this.deaths.intValue()
    			&& this.state.compareTo(c.state) == 0
    			&& this.date.isEqual(c.date));		
    	
    }
    
    @Override
    public int hashCode() 
    { 
    	return this.cases.intValue();
    }

    
}
