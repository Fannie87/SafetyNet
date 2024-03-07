package com.safetynet.applisafety.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.controller.repository.Repository;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.Person;

@ExtendWith(MockitoExtension.class)
public class ControllerPersonTest {
	@InjectMocks
	private ControllerPerson controllerPerson;

	@Mock
	private Repository serviceJSON;

	static JsonData jsonData;

	@BeforeAll
	public static void beforeAll() throws IOException {
		String content = Files.readString(Paths.get("src/test/resources/data-test.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		jsonData = objectMapper.readValue(content, JsonData.class);
	}
	
	@Test
	void postMapping() throws Exception {
		when(serviceJSON.loadJSONData()).thenReturn(jsonData);

		Person personParam = new Person();
		
		personParam.setFirstName("Fannie");
		personParam.setLastName("General");
		personParam.setAddress("1509 Culver St");
		personParam.setCity("Panazol");
		personParam.setZip("97451");
		personParam.setPhone("841-874-6544");
		personParam.setEmail("fg@email.com");
		
		List<Person> listPerson = controllerPerson.addPerson(personParam);
		
		assertThat(listPerson).contains(personParam);
		
	}
	
	@Test
	void putMapping() throws Exception {
		when(serviceJSON.loadJSONData()).thenReturn(jsonData);

		Person personParam = new Person();
		
		personParam.setFirstName("John");
		personParam.setLastName("Boyd");
		personParam.setAddress("1509 Culver St");
		personParam.setCity("Panazol");
		personParam.setZip("97451");
		personParam.setPhone("841-874-6544");
		personParam.setEmail("fg@email.com");
		
		List<Person> listPerson = controllerPerson.updatePerson(personParam);
		
		for (Person person : listPerson) {
			if (person.getFirstName().equals(personParam.getFirstName())&&
					person.getLastName().equals(personParam.getLastName())) {
				
				assertThat(person.getAddress()).isEqualTo("1509 Culver St");
				assertThat(person.getCity()).isEqualTo("Panazol");
				assertThat(person.getEmail()).isEqualTo("fg@email.com");
				assertThat(person.getPhone()).isEqualTo("841-874-6544");
				assertThat(person.getZip()).isEqualTo("97451");
			}
		}
	}
	
	@Test
	void deleteMapping() throws Exception {
		when(serviceJSON.loadJSONData()).thenReturn(jsonData);

		Person personParam = new Person();
		
		personParam.setFirstName("John");
		personParam.setLastName("Boyd");
		
		List<Person> listPerson = controllerPerson.deletePerson(personParam);
		
		assertThat(listPerson).doesNotContain(personParam);
		
	}

}
