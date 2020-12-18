package com.ferfox1981.springbatchrest.configuration.tasklets;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.entity.Measurements;
import com.ferfox1981.springbatchrest.entity.MovingAverageDay;

public class ProcessDataTasklet implements Tasklet, StepExecutionListener{

	
	private Measurements  updated;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.updated = (Measurements)(stepExecution.getJobExecution().getExecutionContext().get("updated"));
		
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		List<MovingAverageDay> results = this.calculateMovingAverage();
		return RepeatStatus.FINISHED;
	}
	
	private List<MovingAverageDay> calculateMovingAverage(){
		List<MovingAverageDay> results = new ArrayList<MovingAverageDay>();
		
			for (int i = 0; i < this.updated.covidMeasurements.size(); i++) {
				try {
				CovidData day = this.updated.covidMeasurements.get(i);
				CovidData dayMinusOne = this.updated.covidMeasurements.get(i+1);
				MovingAverageDay ma = new MovingAverageDay();
				ma.setDeaths(day.getDeaths()-dayMinusOne.getDeaths());
				ma.setDate(day.getDate());
				
				results.add(ma);
				}catch(Exception e) {
					break;
				}
			}
	
		
		
			for (int i = 0; i < results.size(); i++) {
				try {
				int mv1 = results.get(i).getDeaths();
				int mv2 = results.get(i+1).getDeaths();
				int mv3 = results.get(i+2).getDeaths();
				int mv4 = results.get(i+3).getDeaths();
				int mv5 = results.get(i+4).getDeaths();
				int mv6 = results.get(i+5).getDeaths();
				int mv7 = results.get(i+6).getDeaths();
				results.get(i).setMovAvgDay(new Float(mv1+mv2+mv3+mv4+mv5+mv6+mv7)/7);
				
				}catch (Exception e) {
					break;
				}
				
			}
	

		
		return results;
		
	}

}
