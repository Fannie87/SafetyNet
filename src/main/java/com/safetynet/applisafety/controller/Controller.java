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
	// stationNumber = 1
	public FireStationWithCountdown fireStations(@RequestParam @NonNull Integer stationNumber) throws IOException {
		logger.info("/firestation, parametres : stationNumber=" + stationNumber);

		JsonData database = serviceJSON.getJSONFile();

		List<FireStation> firestations = database.getFirestations();
		List<Person> persons = database.getPersons();
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

		List<Person> personsFiltered = new ArrayList<>();

		String adressFireStationFound = null;
		for (FireStation fireStation : firestations)
			if (stationNumber == fireStation.getStation()) {
				adressFireStationFound = fireStation.getAddress();
				break;
			}

		for (Person person : persons)
			if (person.getAddress().equals(adressFireStationFound))
				personsFiltered.add(person);

		LocalDate minDateToBeMajor = LocalDate.now().minusYears(AGE_MAJORITE);
		int numberMinor = 0;
		int numberAdult = 0;
		for (Person person : personsFiltered) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName())
						&& person.getLastName().equals(medicalRecord.getLastName())) {
					if (getBirthdate(medicalRecord).isAfter(minDateToBeMajor)) {// mineur
						numberMinor++;
					} else {
						numberAdult++;
					}
				}
			}
		}

		if (personsFiltered.isEmpty())
			logger.error("La liste des personnes est vide");

		FireStationWithCountdown fireStationWithCountdown = new FireStationWithCountdown();
		fireStationWithCountdown.setNumberAdult(numberAdult);
		fireStationWithCountdown.setNumberMinor(numberMinor);
		fireStationWithCountdown.setPersonsFiltered(personsFiltered);

		logger.info("/firestation, retour : " + new ObjectMapper().writeValueAsString(fireStationWithCountdown));
		return fireStationWithCountdown;
	}

	@GetMapping("/childAlert")
	public List<ChildAlert> childAlert(@RequestParam @NonNull String addressParam) throws IOException {

		logger.info("/childAlert, parametres : addressParam=" + addressParam);

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
		if (!childAlerts.isEmpty()) {//Si il y a des enfants, ils s'enregistrent dans la liste
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

		if (childAlerts.isEmpty())
			logger.error("La liste des enfants est vide");

		logger.info("/childAlert, retour : " + new ObjectMapper().writeValueAsString(childAlerts));
		return childAlerts;
	}

	@GetMapping("/phoneAlert")
	public List<String> phoneAlert(@RequestParam @NonNull Integer firestationNumber) throws IOException {

		logger.info("/phoneAlert, parametres : firestationNumber=" + firestationNumber);

		JsonData database = serviceJSON.getJSONFile();

		List<Person> persons = database.getPersons();
		List<FireStation> fireStations = database.getFirestations();
		List<String> phones = new ArrayList<>();
		List<String> listAddress = new ArrayList<String>();

		for (FireStation fireStation : fireStations)
			if (fireStation.getStation() == firestationNumber)
				listAddress.add(fireStation.getAddress());

		for (String address : listAddress) 
			for (Person person : persons) 
				if (address.equals(person.getAddress())) 
					phones.add(person.getPhone());

		if (phones.isEmpty())
			logger.error("La liste des numéros de téléphone est vide");

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

		FireStation fireStationFound = new FireStation();
		for (FireStation fireStation : fireStations) {
			if (fireStation.getAddress().equals(address)) {
				fireStationFound = fireStation;
				fire.setStation(fireStationFound.getStation());
				break;// fait sortir de la boucle for each après avoir trouvé la station souhaitée
			}
		}

		Map<String, FirePerson> mapPersonFound = new HashMap<String, FirePerson>();// Permet d'éviter une imbrication de
																					// 3 boucles for each
		for (Person person : persons) {
			if (person.getAddress().equals(address)) {
				FirePerson firePerson = new FirePerson();
				firePerson.setFirstName(person.getFirstName());
				firePerson.setLastName(person.getLastName());
				firePerson.setPhone(person.getPhone());

				mapPersonFound.put(person.getFirstName() + person.getLastName(), firePerson);
			}
		}

		for (MedicalRecord medicalRecord : medicalRecords) {
			String keyPerson = medicalRecord.getFirstName() + medicalRecord.getLastName();
			if (mapPersonFound.containsKey(keyPerson)) {
				FirePerson firePerson = mapPersonFound.get(keyPerson);
				int age = Period.between(getBirthdate(medicalRecord), LocalDate.now()).getYears();
				firePerson.setAge(age);
				firePerson.setMedications(medicalRecord.getMedications());
				firePerson.setAllergies(medicalRecord.getAllergies());
			}
		}

		fire.setFirePersons(new ArrayList<>(mapPersonFound.values()));// Demande de renvoyer une liste et non une map=
																		// modification

		if (fire.getFirePersons().isEmpty())
			logger.error("La liste des personnes est vide");

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
									List<FloodPerson> floodsPersons = List.of(floodPerson);
									floods.put(person.getAddress(), floodsPersons);
								}

							}
						}
					}
				}

			}
		}

		if (floods.isEmpty())
			logger.error("La liste des personnes est vide");

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

		if (personInfos.isEmpty())
			logger.error("La liste des personnes est vide");

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

		if (emails.isEmpty())
			logger.error("La liste des mails est vide");

		logger.info("/communityEmail, retour : " + new ObjectMapper().writeValueAsString(emails));
		return emails;
	}

	private LocalDate getBirthdate(MedicalRecord medicalRecord) {
		return LocalDate.parse(medicalRecord.getBirthdate(), formatter);
	}
}
