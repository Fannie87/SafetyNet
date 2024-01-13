package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.applisafety.model.Database;
import com.safetynet.applisafety.model.FireStation;
import com.safetynet.applisafety.model.MedicalRecord;
import com.safetynet.applisafety.model.Person;

@RestController
public class Controller {

	@GetMapping("/firestation")
	public List<FireStation> fireStations(int stationNumber) throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


		Database database = objectMapper.readValue(content, Database.class);

		List<FireStation> firestations = database.getFirestations();
		List<Person> persons = database.getPersons();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

		List<Person> personsFiltered = new ArrayList<>();

		for (FireStation fireStation : firestations) {
			if (stationNumber == fireStation.getStation()) {
				for (Person person : persons) {
					if (person.getAddress().equals(fireStation.getAddress())) {
						personsFiltered.add(person);
					}
				}
			}
		}

		LocalDateTime today = LocalDateTime.now().minusYears(18);

		for (Person person : personsFiltered) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName())
						&& person.getLastName().equals(medicalRecord.getLastName())) {

					medicalRecord.getBirthdate();

				}
			}
		}

		return null;
	}

}
