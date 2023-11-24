package com.safetynet.safetynetalerts;

import com.safetynet.safetynetalerts.data.DataMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetynetalertsApplication {

	private static DataMapper dataMapper = new DataMapper();

	public static void main(String[] args) {
		SpringApplication.run(SafetynetalertsApplication.class, args);
		dataMapper.read();
	}

}
