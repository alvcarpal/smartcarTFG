package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.example"},exclude = {SecurityAutoConfiguration.class })
public class KeyrockserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyrockserviceApplication.class, args);
	}

}
