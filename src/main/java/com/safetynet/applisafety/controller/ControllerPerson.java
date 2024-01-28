package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.Person;
import com.safetynet.applisafety.utils.ServiceJSON;

@RestController
public class ControllerPerson {

	@PostMapping("/person")
	List<Person> postMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();
		
		database.getPersons().add(personParam);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getPersons();
	}
	
	@PutMapping("/person")
	List<Person> putMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();
		
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
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getPersons();
	}
	
	@DeleteMapping("/person")
	List<Person> deleteMapping(@RequestBody Person personParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();
		
		List<Person> persons = database.getPersons();
		
//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		persons.removeIf(person -> person.getFirstName().equals(personParam.getFirstName())&&
				person.getLastName().equals(personParam.getLastName()));
		
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getPersons();
	}
	
}
