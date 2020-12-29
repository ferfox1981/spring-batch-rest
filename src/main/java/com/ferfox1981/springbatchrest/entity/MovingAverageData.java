package com.ferfox1981.springbatchrest.entity;

import java.io.Serializable;
import java.util.List;

public class MovingAverageData implements Serializable {
	
	public List<MovingAverageDay> results;
	
	public List<MovingAverageDay> getResults() {
		return results;
	}

	public void setResults(List<MovingAverageDay> results) {
		this.results = results;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MovingAverageData() {
		
	}

}
