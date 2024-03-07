package com.safetynet.applisafety.controller.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safetynet.applisafety.controller.repository.FireStationRepository;
import com.safetynet.applisafety.controller.repository.MedicalRecordRepository;
import com.safetynet.applisafety.controller.repository.PersonRepository;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FirePerson;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.model.json.Person;

@Component
public class ServiceController {

	private static final int AGE_MAJORITE = 18;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private FireStationRepository fireStationRepository;
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	// 1ère URL
	public FireStationWithCountdown fireStations(Integer stationNumber) throws IOException {

		List<FireStation> firestations = fireStationRepository.getFireStations();
		List<Person> persons = personRepository.getPersons();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

		List<Person> personsFiltered = new ArrayList<>();

		List<String> adressFireStationFounds = new ArrayList<>();
		for (FireStation fireStation : firestations)
			if (stationNumber == fireStation.getStation()) {
				adressFireStationFounds.add(fireStation.getAddress());
			}

		for (Person person : persons)
			for (String address : adressFireStationFounds) 
				if (person.getAddress().equals(address))
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

		FireStationWithCountdown fireStationWithCountdown = new FireStationWithCountdown();
		fireStationWithCountdown.setNumberAdult(numberAdult);
		fireStationWithCountdown.setNumberMinor(numberMinor);
		fireStationWithCountdown.setPersonsFiltered(personsFiltered);

		return fireStationWithCountdown;
	}

	// 2ème URL

	public List<ChildAlert> childAlert(String addressParam) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

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
		if (!childAlerts.isEmpty()) {// Si il y a des enfants, ils s'enregistrent dans la liste
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

//	3ème URL
	public List<String> phoneAlert(Integer firestationNumber) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<FireStation> fireStations = fireStationRepository.getFireStations();
		List<String> phones = new ArrayList<>();
		List<String> listAddress = new ArrayList<String>();

		for (FireStation fireStation : fireStations)
			if (fireStation.getStation() == firestationNumber)
				listAddress.add(fireStation.getAddress());

		for (String address : listAddress)
			for (Person person : persons)
				if (address.equals(person.getAddress()))
					phones.add(person.getPhone());

		return phones;

	}

//	4ème URL
	public Fire fire(String address) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<FireStation> fireStations = fireStationRepository.getFireStations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
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
		return fire;
	}

	// 5ème URL
	public Map<String, List<FloodPerson>> flood(List<Integer> stations) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<FireStation> fireStations = fireStationRepository.getFireStations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

		Map<String, List<FloodPerson>> floods = new HashMap<String, List<FloodPerson>>();

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
									List<FloodPerson> floodsPersons = new ArrayList<FloodPerson>();
									floodsPersons.add(floodPerson);
									floods.put(person.getAddress(), floodsPersons);
								}

							}
						}
					}
				}

			}
		}

		return floods;
	}

	// 6ème URL
	public List<PersonInfo> personInfo(String firstName, String lastName) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
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

		return personInfos;
	}

	// 7ème URL
	public List<String> communityEmail(String city) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<String> emails = new ArrayList<>();

		for (Person person : persons) {
			if (person.getCity().equals(city)) {
				emails.add(person.getEmail());
			}
		}

		return emails;
	}

	private LocalDate getBirthdate(MedicalRecord medicalRecord) {
		return LocalDate.parse(medicalRecord.getBirthdate(), formatter);
	}

}
