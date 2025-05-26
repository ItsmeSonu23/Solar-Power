package com.solar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolarPowerApplication {

	public static void main(String[] args) {
		EnvLoader.load(); // Load environment variables
		SpringApplication.run(SolarPowerApplication.class, args);
	}

}
