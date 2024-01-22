package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.model.Database;
import com.safetynet.applisafety.model.Person;
import com.safetynet.applisafety.utils.ServiceJSON;

@RestController
public class ControllerPerson {

	@PostMapping
	Person postMapping(Person person) throws IOException {
		Database database = ServiceJSON.createDatabase();
		
		database.getPersons().add(person);
//		Files.write("src/main/resources/data.json", database, null)
		return null;
	}
	
	//@deletemapping
	//@put 
}
