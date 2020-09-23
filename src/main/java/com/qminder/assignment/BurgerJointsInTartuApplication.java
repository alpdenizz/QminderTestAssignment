package com.qminder.assignment;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * <p> <b>denizalp kapisiz</b> [denizalp1634@hotmail.com] </p>
 * <p> Application starting point </p>
 *
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BurgerJointsInTartuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BurgerJointsInTartuApplication.class, args);
	}
	
	/**
	 * 
	 * Asynchronous Task Executor for getting burger joints with the latest picture
	 */
	@Bean(name="asyncExec")
    public Executor taskExecutor() {
	   ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	   executor.setCorePoolSize(5);
	   executor.setMaxPoolSize(5);
	   executor.setQueueCapacity(500);
	   executor.setThreadNamePrefix("BurgerJointPhotoLookup-");
	   executor.initialize();
	   return executor;
	}

}
