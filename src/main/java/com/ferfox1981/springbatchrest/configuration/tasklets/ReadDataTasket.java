package com.ferfox1981.springbatchrest.configuration.tasklets;



import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;


import com.ferfox1981.springbatchrest.entity.Measurements;
import com.ferfox1981.springbatchrest.integration.covidcenter.ExternalConsumer;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class ReadDataTasket implements Tasklet, StepExecutionListener{

	Measurements sourceData = new Measurements();
	
	@Autowired
	private ExternalConsumer ec;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		/*
		FirebaseOptions options = FirebaseOptions.builder()
			    .setCredentials(GoogleCredentials.getApplicationDefault())
			    .setDatabaseUrl("https://teste-c5687-default-rtdb.firebaseio.com/")
			    .build();

			FirebaseApp.initializeApp(options);
		*/
		sourceData.setCovidMeasurements(ec.getDaysData());
		return RepeatStatus.FINISHED;
	}
	
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        
        stepExecution
          .getJobExecution()
          .getExecutionContext()
          .put("data", sourceData);
        
        return ExitStatus.COMPLETED;
    }

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}
}
	
	


