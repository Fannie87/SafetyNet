package com.safetynet.applisafety.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.applisafety.model.json.Person;
import com.safetynet.applisafety.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)

public class PersonRepositoryTest {
	@InjectMocks
	private PersonRepository personRepository;
	
	@Test
	void addPerson() throws Exception {
		personRepository.loadJSONData();
		Person person = new Person ();
		
		List<Person> persons = personRepository.addPerson(person);
		
		assertThat(persons.size()).isEqualTo(24);
	}
	
	@Test
	void deletePerson() throws Exception {
		personRepository.loadJSONData();
		Person person = new Person ();
		person.setFirstName("John");
		person.setLastName("Boyd");

		List<Person> persons = personRepository.deletePerson(person);

		assertThat(persons.size()).isEqualTo(22);
	}
	
	@Test
	void updatePerson() throws Exception {
		personRepository.loadJSONData();
		Person person = new Person ();
		person.setFirstName("John");
		person.setLastName("Boyd");
		person.setAddress("1509 Culver St");
		person.setCity("Culver");

		List<Person> persons = personRepository.updatePerson(person);

		assertThat(persons.get(0).getAddress()).isEqualTo("1509 Culver St");
	}
	
}
