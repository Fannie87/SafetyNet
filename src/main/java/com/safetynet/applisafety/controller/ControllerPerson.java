package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.Person;

@RestController
public class ControllerPerson {
	@Autowired
	private ServiceJSON serviceJSON;
	
	@PostMapping("/person")
	List<Person> postMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();
		
		database.getPersons().add(personParam);
		serviceJSON.updateDatabase(database);

		return database.getPersons();
	}
	
	@PutMapping("/person")
	List<Person> putMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();
		
		List<Person> persons = database.getPersons();
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
		
		serviceJSON.updateDatabase(database);

		return database.getPersons();
	}
	
	@DeleteMapping("/person")
	List<Person> deleteMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();
		
		List<Person> persons = database.getPersons();
		
//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		persons.removeIf(person -> person.getFirstName().equals(personParam.getFirstName())&&
				person.getLastName().equals(personParam.getLastName()));
		
		
		serviceJSON.updateDatabase(database);

		return database.getPersons();
	}
	
}
