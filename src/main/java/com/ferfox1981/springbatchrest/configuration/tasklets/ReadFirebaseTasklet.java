package com.ferfox1981.springbatchrest.configuration.tasklets;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.ferfox1981.springbatchrest.adapter.LocalDateTimeAdapter;
import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.entity.Measurements;
import com.ferfox1981.springbatchrest.integration.covidcenter.ExternalConsumer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.stream.Collectors;
import java.util.stream.Stream; 

public class ReadFirebaseTasklet implements Tasklet, StepExecutionListener{
	
	@Autowired
	private ExternalConsumer ec;
	
	private Measurements  current;
	private Measurements  updated;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.current = (Measurements)(stepExecution.getJobExecution().getExecutionContext().get("data"));
		
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put("updated", updated);
      
      return ExitStatus.COMPLETED;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try {
			String  base = ec.getFireBaseData();
			if ("null".compareTo(base) == 0) {
				ec.saveToFireBase(this.current);
			} else {
								
				Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
				Measurements existing = gson.fromJson(base, new TypeToken<Measurements>() {}.getType());				

				this.updated = new Measurements();
				List<CovidData> x = Stream.concat(this.current.covidMeasurements.stream(), existing.covidMeasurements.stream()).distinct().collect(Collectors.toList());
				Collections.sort(x, new Comparator<CovidData>() {
					@Override
					public int compare(CovidData o1, CovidData o2) {
						
						if(o1.getDate().isBefore(o2.getDate()))
							return -1;
						if (o1.getDate().isEqual(o2.getDate()))
							return 0;
						else
							return 1;
						
					}
				});
				Collections.reverse(x);
				this.updated.covidMeasurements = x;
				ec.saveToFireBase(this.updated);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RepeatStatus.FINISHED;
	}

}
