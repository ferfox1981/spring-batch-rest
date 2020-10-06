package com.ferfox1981.springbatchrest.processor;

import java.util.List;

import com.ferfox1981.springbatchrest.entity.CovidData;

import org.springframework.batch.item.ItemProcessor;

public class CovidDataProcessor implements ItemProcessor<List<CovidData>, List<CovidData>> {

	@Override
	public List<CovidData> process(final List<CovidData> item) throws Exception {
        return null;
    }

    
}
