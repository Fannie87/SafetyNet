package com.safetynet.applisafety;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.safetynet.applisafety.repository.FireStationRepository;
import com.safetynet.applisafety.repository.MedicalRecordRepository;
import com.safetynet.applisafety.repository.PersonRepository;

@SpringBootApplication
public class ApplisafetyApplication implements CommandLineRunner {

	@Autowired
	private FireStationRepository fireStationRepository;
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	@Autowired
	private PersonRepository personRepository;

	
	public static void main(String[] args) {
		SpringApplication.run(ApplisafetyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fireStationRepository.loadJSONData();
		medicalRecordRepository.loadJSONData();
		personRepository.loadJSONData();
	}

}
