package com.safetynet.applisafety.controller.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.safetynet.applisafety.controller.repository.PersonRepository;
import com.safetynet.applisafety.model.json.Person;

@Service
public class ServiceControllerPerson {

	@Autowired
	private PersonRepository personRepository;

	public List<Person> addPerson(Person personParam) throws IOException {
		return personRepository.addPerson(personParam);
	}

	public List<Person> updatePerson(Person personParam) throws IOException {
		return personRepository.updatePerson(personParam);
	}

	public List<Person> deletePerson(@RequestBody Person personParam) throws IOException {
		return personRepository.deletePerson(personParam);
	}

}
