package com.safetynet.applisafety.controller.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.applisafety.controller.repository.FireStationRepository;
import com.safetynet.applisafety.controller.repository.MedicalRecordRepository;
import com.safetynet.applisafety.controller.repository.PersonRepository;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FirePerson;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.model.json.Person;
import com.safetynet.applisafety.utils.Utils;

@Service
public class ServiceControllerFireStation {
	
	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public List<FireStation> addFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.addFireStation(fireStationParam);
	}

	public List<FireStation> updateFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.updateFireStation(fireStationParam);
	}

	public List<FireStation> deleteFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.deleteFireStation(fireStationParam);
	}

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

		LocalDate minDateToBeMajor = LocalDate.now().minusYears(Utils.AGE_MAJORITE);
		int numberMinor = 0;
		int numberAdult = 0;
		for (Person person : personsFiltered) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName())
						&& person.getLastName().equals(medicalRecord.getLastName())) {
					if (Utils.calculateBirthdate(medicalRecord).isAfter(minDateToBeMajor)) {// mineur
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

	// 3ème URL
	public List<String> phoneAlert(Integer firestation) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<FireStation> fireStations = fireStationRepository.getFireStations();
		List<String> phones = new ArrayList<>();
		List<String> listAddress = new ArrayList<String>();

		for (FireStation fireStation : fireStations)
			if (fireStation.getStation() == firestation)
				listAddress.add(fireStation.getAddress());

		for (String address : listAddress)
			for (Person person : persons)
				if (address.equals(person.getAddress()))
					phones.add(person.getPhone());

		return phones;

	}

	// 4ème URL
	public Fire fire(String address) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<FireStation> fireStations = fireStationRepository.getFireStations();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
		Fire fire = new Fire();
		fire.setFirePersons(new ArrayList<>());

		FireStation fireStationFound = null;
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
				int age = Period.between(Utils.calculateBirthdate(medicalRecord), LocalDate.now()).getYears();
				firePerson.setAge(age);
				firePerson.setMedications(medicalRecord.getMedications());
				firePerson.setAllergies(medicalRecord.getAllergies());
			}
		}

		fire.setFirePersons(new ArrayList<>(mapPersonFound.values()));// Demande de renvoyer une liste et non une map=
																		// modification
		return fire;
	}

}
