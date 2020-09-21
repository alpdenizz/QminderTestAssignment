package com.qminder.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BurgerJointsInTartuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BurgerJointsInTartuApplication.class, args);
	}

}
