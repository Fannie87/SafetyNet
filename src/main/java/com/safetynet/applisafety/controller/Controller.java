package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.Database;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FirePerson;
import com.safetynet.applisafety.model.FireStation;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.MedicalRecord;
import com.safetynet.applisafety.model.Person;

@RestController
public class Controller {
	// 1ère URL
	@GetMapping("/firestation")
	// GET - /firestation/1
	// stationNumber = 1
	public FireStationWithCountdown fireStations(@RequestParam int stationNumber) throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
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

		LocalDate today = LocalDate.now().minusYears(18);
		int numberMinor = 0;
		int numberAdult = 0;
		for (Person person : personsFiltered) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName())
						&& person.getLastName().equals(medicalRecord.getLastName())) {
					if (medicalRecord.getBirthdate().isAfter(today)) {// mineur
						numberMinor++;
					} else {
						numberAdult++;
					}
				}
			}
		}
		FireStationWithCountdown fireStationWithCountdown = new FireStationWithCountdown();
		fireStationWithCountdown.setNumberAdult(numberAdult);
		fireStationWithCountdown.setNumberMinor(numberMinor);
		fireStationWithCountdown.setPersonsFiltered(personsFiltered);
		return fireStationWithCountdown;
	}

	@GetMapping("/childAlert")
	public List<ChildAlert> childAlert(String addressParam) throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		Database database = objectMapper.readValue(content, Database.class);

		List<Person> persons = database.getPersons();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

		List<ChildAlert> childAlerts = new ArrayList<>();

		LocalDate dateOfMajority = LocalDate.now().minusYears(18);

		for (Person person : persons) {
			if (person.getAddress().equals(addressParam)) {
				for (MedicalRecord medicalRecord : medicalRecords) {
					if (person.getFirstName().equals(medicalRecord.getFirstName())
							&& person.getLastName().equals(medicalRecord.getLastName())) {
						if (medicalRecord.getBirthdate().isAfter(dateOfMajority)) {
							int age = Period.between(medicalRecord.getBirthdate(), LocalDate.now()).getYears();
							ChildAlert childAlert = new ChildAlert();
							childAlert.setFirstName(person.getFirstName());
							childAlert.setLastName(person.getLastName());
							childAlert.setAge(age);
							childAlerts.add(childAlert);
						}
					}
				}
			}
		}
		if (childAlerts != null) {
			for (Person person : persons) {
				if (person.getAddress().equals(addressParam)) {
					for (MedicalRecord medicalRecord : medicalRecords) {
						if (person.getFirstName().equals(medicalRecord.getFirstName())
								&& person.getLastName().equals(medicalRecord.getLastName())
								&& medicalRecord.getBirthdate().isBefore(dateOfMajority)) {
							int age = Period.between(medicalRecord.getBirthdate(), LocalDate.now()).getYears();
							ChildAlert childAlert = new ChildAlert();
							childAlert.setFirstName(person.getFirstName());
							childAlert.setLastName(person.getLastName());
							childAlert.setAge(age);
							childAlerts.add(childAlert);
						}
					}
				}
			}
		}

		return childAlerts;
	}

	@GetMapping("/phoneAlert")
	public List<String> phoneAlert(int firestationNumber) throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		Database database = objectMapper.readValue(content, Database.class);

		List<Person> persons = database.getPersons();
		List<FireStation> fireStations = database.getFirestations();
		List<String> phones = new ArrayList<>();

		for (FireStation fireStation : fireStations) {
			for (Person person : persons) {
				if (fireStation.getAddress().equals(person.getAddress())
						&& fireStation.getStation() == firestationNumber) {
					phones.add(person.getPhone());
				}
			}
		}

		return phones;

	}

	@GetMapping("/fire")
	public  Fire fire(String address) throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		Database database = objectMapper.readValue(content, Database.class);

		List<Person> persons = database.getPersons();
		List<FireStation> fireStations = database.getFirestations();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();
		Fire fire = new Fire();
		fire.setFirePersons(new ArrayList<>());

		for (Person person : persons) {
			for (FireStation fireStation : fireStations) {
				for (MedicalRecord medicalRecord : medicalRecords) {
					if (person.getAddress().equals(fireStation.getAddress()) && fireStation.getAddress().equals(address)
							&& person.getFirstName().equals(medicalRecord.getFirstName())
							&& person.getLastName().equals(medicalRecord.getLastName())) {
						int age = Period.between(medicalRecord.getBirthdate(), LocalDate.now()).getYears();
						FirePerson firePerson = new FirePerson();

						firePerson.setFirstName(person.getFirstName());
						firePerson.setLastName(person.getLastName());
						firePerson.setPhone(person.getPhone());
						firePerson.setAge(age);
						firePerson.setMedications(medicalRecord.getMedications());
						firePerson.setAllergies(medicalRecord.getAllergies());
						fire.setStation(fireStation.getStation());
						fire.getFirePersons().add(firePerson);
					}
				}
			}
		}
		return fire;
	}
//	@GetMapping("/flood")
//	public  Flood flood (String address) throws IOException {
//		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
//		ObjectMapper objectMapper = new ObjectMapper();
//		Database database = objectMapper.readValue(content, Database.class);
//
//		List<Person> persons = database.getPersons();
//		List<FireStation> fireStations = database.getFirestations();
//		List<MedicalRecord> medicalRecords = database.getMedicalrecords();
//		
//		for (Person person : persons) {
//			for (MedicalRecord medicalRecord : medicalRecords) {
//				for (FireStation fireStation : fireStations) {
//					
//				}
//			}
//			
//		}
//		
//	}

}
