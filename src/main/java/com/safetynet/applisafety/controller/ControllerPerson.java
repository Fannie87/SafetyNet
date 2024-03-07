package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.safetynet.applisafety.controller.service.ServiceControllerPerson;
import com.safetynet.applisafety.model.json.Person;

@Component
public class ControllerPerson {
	
	
	@Autowired
	private ServiceControllerPerson serviceControllerPerson;
	
	@PostMapping("/person")
	List<Person> addPerson(@RequestBody Person personParam) throws IOException {
		return serviceControllerPerson.addPerson(personParam);
	}
	
	@PutMapping("/person")
	List<Person> updatePerson(@RequestBody Person personParam) throws IOException {
		return serviceControllerPerson.updatePerson(personParam);
	}
	
	@DeleteMapping("/person")
	List<Person> deletePerson(@RequestBody Person personParam) throws IOException {
		return serviceControllerPerson.deletePerson(personParam);
	}
	
}
