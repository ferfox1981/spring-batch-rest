package com.ferfox1981.springbatchrest.configuration;

import java.util.List;

import javax.sql.DataSource;

import com.ferfox1981.springbatchrest.configuration.tasklets.ReadDataTasket;
import com.ferfox1981.springbatchrest.configuration.tasklets.ReadFirebaseTasklet;
import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.integration.covidcenter.ExternalConsumer;
import com.ferfox1981.springbatchrest.integration.firebase.FirebaseConsumer;
import com.ferfox1981.springbatchrest.integration.firebase.FirebaseProducer;
import com.ferfox1981.springbatchrest.processor.CovidDataProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	public Tasklet getFirebaseTasklet() {
		return new ReadFirebaseTasklet();
	}
	
	@Bean
	public Tasklet getReadTasklet() {
		return new ReadDataTasket();
	}
	
	@Bean
	public Job job() {
		return jobs.get("job").
				preventRestart().
				start(readDataTasklet()).
				next(processDataTasklet()).
				next(saveDataTasklet()).
				build();
	}	

	public Step readDataTasklet() {
		return stepBuilderFactory.get("step1").tasklet(getReadTasklet()).build();
	}	
	

	public Step processDataTasklet() {
		return stepBuilderFactory.get("step2").tasklet(getFirebaseTasklet()).build();
	}	
	
	public Step saveDataTasklet() {
		return stepBuilderFactory.get("step3").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("TRES");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}	


	@Bean
	public ItemReaderAdapter<List<CovidData>> dataReader() {
		ItemReaderAdapter<List<CovidData>> reader = new ItemReaderAdapter<List<CovidData>>();
		reader.setTargetObject(fooService());
		reader.setTargetMethod("processFoo");
		return reader;

	}	

	@Bean 
	public FirebaseConsumer fooService(){
		return new FirebaseConsumer();
	}



	@Bean
	public CovidDataProcessor processor() {
		
		return new CovidDataProcessor();
	}	


	@Bean
	public ItemWriterAdapter<List<CovidData>> itemWriter() {
		ItemWriterAdapter<List<CovidData>> writer = new ItemWriterAdapter<List<CovidData>>();
	
		writer.setTargetObject(producerService());
		writer.setTargetMethod("produceFoo");
	
		return writer;
	}	

	@Bean 
	public FirebaseProducer producerService(){
		return new FirebaseProducer();
	}	

    @Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
    }
    
    
}
