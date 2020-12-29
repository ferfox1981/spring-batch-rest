package com.ferfox1981.springbatchrest.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ferfox1981.springbatchrest.configuration.tasklets.ProcessDataTasklet;
import com.ferfox1981.springbatchrest.configuration.tasklets.ReadDataTasket;
import com.ferfox1981.springbatchrest.configuration.tasklets.ReadFirebaseTasklet;
import com.ferfox1981.springbatchrest.integration.covidcenter.ExternalConsumer;
import com.ferfox1981.springbatchrest.processor.CovidDataProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ExternalConsumer ec;
	
	@Bean
	public Tasklet getReadTasklet() {
		return new ReadDataTasket();
	}	
	
	@Bean 
	public Tasklet getFirebaseTasklet() {
		return new ReadFirebaseTasklet();
	}
	
	@Bean
	public Tasklet getProcesserTasklet() {
		return new ProcessDataTasklet();
	}
	

	
	@Bean
	public Job job() {
		return jobs.get("job").
				preventRestart().
				start(readSourceDataTasklet()).
				next(readLocalDataTasklet()).
				next(saveDataTasklet()).
				build();
	}	

	public Step readSourceDataTasklet() {
		return stepBuilderFactory.get("step1").tasklet(getReadTasklet()).build();
	}	
	

	public Step readLocalDataTasklet() {
		return stepBuilderFactory.get("step2").tasklet(getFirebaseTasklet()).build();
	}	
	
	public Step saveDataTasklet() {
		return stepBuilderFactory.get("step3").tasklet(getProcesserTasklet()).build();
	}	



	@Bean
	public CovidDataProcessor processor() {
		
		return new CovidDataProcessor();
	}	



    @Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
    }
    
    
}
