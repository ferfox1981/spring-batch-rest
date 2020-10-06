package com.ferfox1981.springbatchrest.configuration;

import java.util.List;

import javax.sql.DataSource;

import com.ferfox1981.springbatchrest.entity.CovidData;
import com.ferfox1981.springbatchrest.integration.firebase.FirebaseConsumer;
import com.ferfox1981.springbatchrest.integration.firebase.FirebaseProducer;
import com.ferfox1981.springbatchrest.processor.CovidDataProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
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
	
	
	@Bean
	public Job job() {
		return jobs.get("job").preventRestart().start(step1()).build();
	}	


	@Bean
	protected Step step1() {
		
		return this.stepBuilderFactory.get("step1").
				<List<CovidData>, List<CovidData>>chunk(1).
				reader(dataReader()).
				processor(processor()).				
				writer(itemWriter()).
				build();

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
