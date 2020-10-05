package com.ferfox1981.springbatchrest.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {


    

    @Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
    }
    
    
}
