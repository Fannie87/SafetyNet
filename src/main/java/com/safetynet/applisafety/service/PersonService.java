package com.safetynet.applisafety.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.model.json.Person;
import com.safetynet.applisafety.repository.FireStationRepository;
import com.safetynet.applisafety.repository.MedicalRecordRepository;
import com.safetynet.applisafety.repository.PersonRepository;
import com.safetynet.applisafety.utils.Utils;

@Service
public class PersonService {

	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public List<Person> addPerson(Person personParam) throws IOException {
		return personRepository.addPerson(personParam);
	}

	public List<Person> updatePerson(Person personParam) throws IOException {
		return personRepository.updatePerson(personParam);
	}

	public List<Person> deletePerson(@RequestBody Person personParam) throws IOException {
		return personRepository.deletePerson(personParam);
	}

	// 2ème URL
	public List<ChildAlert> childAlert(String address) throws IOException {

		List<Person> persons = personRepository.getPersons();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();

		List<ChildAlert> childAlerts = new ArrayList<>();

		LocalDate dateOfMajority = LocalDate.now().minusYears(Utils.AGE_MAJORITE);

		for (Person person : persons) {
			if (person.getAddress().equals(address)) {
				for (MedicalRecord medicalRecord : medicalRecords) {
					if (person.getFirstName().equals(medicalRecord.getFirstName())
							&& person.getLastName().equals(medicalRecord.getLastName())) {
						if (Utils.parseBirthdate(medicalRecord).isAfter(dateOfMajority)) {
							int age = Period.between(Utils.parseBirthdate(medicalRecord), LocalDate.now()).getYears();
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
				if (person.getAddress().equals(address)) {
					for (MedicalRecord medicalRecord : medicalRecords) {
						if (person.getFirstName().equals(medicalRecord.getFirstName())
								&& person.getLastName().equals(medicalRecord.getLastName())
								&& Utils.parseBirthdate(medicalRecord).isBefore(dateOfMajority)) {
							int age = Period.between(Utils.parseBirthdate(medicalRecord), LocalDate.now()).getYears();
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
								int age = Period.between(Utils.parseBirthdate(medicalRecord), LocalDate.now()).getYears();
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
						int age = Period.between(Utils.parseBirthdate(medicalRecord), LocalDate.now()).getYears();
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

}
