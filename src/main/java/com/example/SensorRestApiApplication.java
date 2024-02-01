package com.example;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SensorRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorRestApiApplication.class, args);

		RestTemplate restTemplate = new RestTemplate();



	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
