package com.safetynet.applisafety.controller.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.safetynet.applisafety.model.json.Person;
import com.safetynet.applisafety.utils.Utils;

@Service
public class PersonRepository {
	private List<Person> persons;

	public void loadJSONData() throws IOException {
		persons = Utils.loadJSONData().getPersons();
	}

	public List<Person> getPersons() {
		return persons;
	}
	
	public List<Person> addPerson(Person personParam) {
		persons.add(personParam);
		return persons;
	}
	
	public List<Person> deletePerson(Person personParam) {
		persons.removeIf(person -> person.getFirstName().equals(personParam.getFirstName())&&
				person.getLastName().equals(personParam.getLastName()));
		return persons;
	}
	
	public List<Person> updatePerson(Person personParam) {
		for (Person person : persons) {
			if (person.getFirstName().equals(personParam.getFirstName())&&
					person.getLastName().equals(personParam.getLastName())) {
				person.setAddress(personParam.getAddress());
				person.setCity(personParam.getCity());
				person.setEmail(personParam.getEmail());
				person.setPhone(personParam.getPhone());
				person.setZip(personParam.getZip());
			}
		}
		return persons;
	}

}
