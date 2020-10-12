package com.ferfox1981.springbatchrest.configuration.tasklets;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.integration.covidcenter.ExternalConsumer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ReadFirebaseTasklet implements Tasklet, StepExecutionListener{
	
	@Autowired
	private ExternalConsumer ec;
	
	private List<CovidData>  current;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.current = (List<CovidData>)(stepExecution.getJobExecution().getExecutionContext().get("data"));
		
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		return null;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try {
			String  base = ec.getFireBaseData();
			if ("null".compareTo(base) == 0) {
				ec.saveToFireBase(this.current);
			} else {
				
				Gson gson = new Gson();
				List<CovidData> outputList = gson.fromJson(base, new TypeToken<List<CovidData>>() {}.getType());
				//ec.updateToFireBase();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
