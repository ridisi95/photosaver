package com.photosaver.photosaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhotosaverApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotosaverApplication.class, args);
	}
}
