package com.sensor.middleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.sensor"},exclude = {SecurityAutoConfiguration.class})
public class MiddleserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiddleserviceApplication.class, args);
	}

}
