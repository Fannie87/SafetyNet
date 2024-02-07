package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FirePerson;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.model.json.Person;

@RestController
public class Controller {

	private static final int AGE_MAJORITE = 18;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static final Logger logger = LogManager.getLogger(Controller.class);;

	@Autowired
	private ServiceJSON serviceJSON;

	// 1ère URL
	@GetMapping("/firestation")
	// GET - /firestation/1
	// stationNumber = 1
	public FireStationWithCountdown fireStations(@RequestParam @NonNull Integer stationNumber) throws IOException {
		logger.info("/firestation, parametres : stationNumber=" + stationNumber);
		
		JsonData database = serviceJSON.getJSONFile();

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

		LocalDate today = LocalDate.now().minusYears(AGE_MAJORITE);
		int numberMinor = 0;
		int numberAdult = 0;
		for (Person person : personsFiltered) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName())
						&& person.getLastName().equals(medicalRecord.getLastName())) {
					if (getBirthdate(medicalRecord).isAfter(today)) {// mineur
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
		
		logger.info("/firestation, retour : " + new ObjectMapper().writeValueAsString(fireStationWithCountdown));
		return fireStationWithCountdown;
	}

	@GetMapping("/childAlert")
	public List<ChildAlert> childAlert(@RequestParam @NonNull String addressParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();

		List<Person> persons = database.getPersons();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

		List<ChildAlert> childAlerts = new ArrayList<>();

		LocalDate dateOfMajority = LocalDate.now().minusYears(AGE_MAJORITE);

		for (Person person : persons) {
			if (person.getAddress().equals(addressParam)) {
				for (MedicalRecord medicalRecord : medicalRecords) {
					if (person.getFirstName().equals(medicalRecord.getFirstName())
							&& person.getLastName().equals(medicalRecord.getLastName())) {
						if (getBirthdate(medicalRecord).isAfter(dateOfMajority)) {
							int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
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
								&& getBirthdate(medicalRecord).isBefore(dateOfMajority)) {
							int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
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
	public List<String> phoneAlert(@RequestParam @NonNull Integer firestationNumber) throws IOException {
		
		logger.info("/phoneAlert, parametres : firestationNumber=" + firestationNumber);
		
		JsonData database = serviceJSON.getJSONFile();

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
		logger.info("/phoneAlert, retour : " + new ObjectMapper().writeValueAsString(phones));
		return phones;

	}

	@GetMapping("/fire")
	public Fire fire(@RequestParam @NonNull String address) throws IOException {
		
		logger.info("/fire, parametres : address=" + address);
		
		JsonData database = serviceJSON.getJSONFile();
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
						int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
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
		logger.info("/fire, retour : " + new ObjectMapper().writeValueAsString(fire));
		return fire;
	}

	// 5ème URL
	@GetMapping("/flood/stations")
	public Map<String, List<FloodPerson>> flood(@RequestParam @NonNull List<Integer> stations) throws IOException {
		
		logger.info("/flood/stations, parametres : stations=" + stations);
		
		JsonData database = serviceJSON.getJSONFile();

		List<Person> persons = database.getPersons();
		List<FireStation> fireStations = database.getFirestations();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

		Map<String, List<FloodPerson>> floods = new HashMap<>();

		for (FireStation fireStation : fireStations) {
			for (Integer station : stations) {
				if (fireStation.getStation() == station) {
					for (Person person : persons) {
						for (MedicalRecord medicalRecord : medicalRecords) {
							if (fireStation.getAddress().equals(person.getAddress())
									&& person.getFirstName().equals(medicalRecord.getFirstName())
									&& person.getLastName().equals(medicalRecord.getLastName())) {
								int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
								FloodPerson floodPerson = new FloodPerson();

								floodPerson.setFirstName(person.getFirstName());
								floodPerson.setLastName(person.getLastName());
								floodPerson.setPhone(person.getPhone());
								floodPerson.setAge(age);
								floodPerson.setLastName(person.getLastName());
								floodPerson.setMedications(medicalRecord.getMedications());
								floodPerson.setAllergies(medicalRecord.getAllergies());
								if (floods.containsKey(person.getAddress())) {
									floods.get(person.getAddress()).add(floodPerson);
								} else {
									List<FloodPerson> floodsPersons = new ArrayList<>();
									floodsPersons.add(floodPerson);
									floods.put(person.getAddress(), floodsPersons);
								}

							}
						}
					}
				}

			}
		}
		
		logger.info("/flood/stations, retour : " + new ObjectMapper().writeValueAsString(floods));
		return floods;
	}

	// 6ème URL
	@GetMapping("/personInfo")
	public List<PersonInfo> personInfo(@RequestParam @NonNull String firstName, @RequestParam @NonNull String lastName)
			throws IOException {
		
		logger.info("/personInfo, parametres : firstName=" + firstName);
		
		JsonData database = serviceJSON.getJSONFile();
		List<Person> persons = database.getPersons();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();
		List<PersonInfo> personInfos = new ArrayList<>();

		for (Person person : persons) {
			if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
				for (MedicalRecord medicalRecord : medicalRecords) {
					if (person.getFirstName().equals(medicalRecord.getFirstName())
							&& person.getLastName().equals(medicalRecord.getLastName())) {
						int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
						PersonInfo personInfo = new PersonInfo();

						personInfo.setFirstName(person.getFirstName());
						personInfo.setLastName(person.getLastName());
						personInfo.setAddress(person.getAddress());
						personInfo.setAge(age);
						personInfo.setEmail(person.getEmail());
						personInfo.setMedications(medicalRecord.getMedications());
						personInfo.setAllergies(medicalRecord.getAllergies());

						personInfos.add(personInfo);
					}
				}
			}
		}
		logger.info("/personInfo, retour : " + new ObjectMapper().writeValueAsString(personInfos));
		return personInfos;
	}

	// 7ème URL
	@GetMapping("/communityEmail")
	public List<String> communityEmail(@RequestParam @NonNull String city) throws IOException {
		
		logger.info("/communityEmail, parametres : city=" + city);
		
		JsonData database = serviceJSON.getJSONFile();

		List<Person> persons = database.getPersons();
		List<String> emails = new ArrayList<>();

		for (Person person : persons) {
			if (person.getCity().equals(city)) {
				emails.add(person.getEmail());
			}
		}
		logger.info("/communityEmail, retour : " + new ObjectMapper().writeValueAsString(emails));
		return emails;
	}

	private LocalDate getBirthdate(MedicalRecord medicalRecord) {
		return LocalDate.parse(medicalRecord.getBirthdate(), formatter);
	}
}
