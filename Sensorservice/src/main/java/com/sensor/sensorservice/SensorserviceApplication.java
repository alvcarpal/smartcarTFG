package com.sensor.sensorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.sensor"},exclude = {SecurityAutoConfiguration.class})
public class SensorserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorserviceApplication.class, args);
	}

}
