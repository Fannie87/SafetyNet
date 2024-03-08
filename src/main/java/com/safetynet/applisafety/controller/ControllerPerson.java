package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.controller.service.ServiceControllerPerson;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.Person;

@RestController
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
	
	@GetMapping("/childAlert")
	public List<ChildAlert> childAlert(@RequestParam @NonNull String address) throws IOException {
		return serviceControllerPerson.childAlert(address);
	}

	@GetMapping("/personInfo")
	public List<PersonInfo> personInfo(@RequestParam @NonNull String firstName, @RequestParam @NonNull String lastName)
			throws IOException {
		return serviceControllerPerson.personInfo(firstName, lastName);
	}
	
	@GetMapping("/communityEmail")
	public List<String> communityEmail(@RequestParam @NonNull String city) throws IOException {
		return serviceControllerPerson.communityEmail(city);
	}

	@GetMapping("/flood/stations")
	public Map<String, List<FloodPerson>> flood(@RequestParam @NonNull List<Integer> stations) throws IOException {
		return serviceControllerPerson.flood(stations);
	}
	
	
	
	
}
