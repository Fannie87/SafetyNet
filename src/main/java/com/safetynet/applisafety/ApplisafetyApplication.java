package com.safetynet.applisafety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.safetynet.applisafety.config.ServiceJSON;

@SpringBootApplication
@Import(ServiceJSON.class)
public class ApplisafetyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplisafetyApplication.class, args);
	}

}
