package com.photosaver.photosaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
//@EnableScheduling
@EnableCaching
@EnableAspectJAutoProxy
public class PhotosaverApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhotosaverApplication.class, args);
	}
}
