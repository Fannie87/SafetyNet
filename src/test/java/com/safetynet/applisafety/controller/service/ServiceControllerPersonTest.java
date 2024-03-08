package com.safetynet.applisafety.controller.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.controller.repository.FireStationRepository;
import com.safetynet.applisafety.controller.repository.MedicalRecordRepository;
import com.safetynet.applisafety.controller.repository.PersonRepository;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.JsonData;
@ExtendWith(MockitoExtension.class)
public class ServiceControllerPersonTest {
	@InjectMocks
	private ServiceControllerPerson serviceControllerPerson;

	@Mock
	private PersonRepository personRepository;
	@Mock
	private FireStationRepository fireStationRepository;
	@Mock
	private MedicalRecordRepository medicalRecordRepository;

	private JsonData jsonData;

	@BeforeEach
	public void beforeEach() throws IOException {
		String content = Files.readString(Paths.get("src/test/resources/data-test.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		jsonData = objectMapper.readValue(content, JsonData.class);

	}

	// 2ème URL
	@Test
	void childAlert() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(medicalRecordRepository.getMedicalRecords()).thenReturn(jsonData.getMedicalrecords());
		
		List<ChildAlert> childAlerts = serviceControllerPerson.childAlert("1509 Culver St");

		assertThat(childAlerts.get(0).getFirstName()).isEqualTo("Tenley");
		assertThat(childAlerts.get(0).getLastName()).isEqualTo("Boyd");
		assertThat(childAlerts.get(0).getAge()).isEqualTo(12);
	}



	// 5ème Test
	@Test
	void flood() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(fireStationRepository.getFireStations()).thenReturn(jsonData.getFirestations());
		when(medicalRecordRepository.getMedicalRecords()).thenReturn(jsonData.getMedicalrecords());
		
		List<Integer> stations = new ArrayList<Integer>();
		stations.add(1);
		stations.add(2);
		Map<String, List<FloodPerson>> floods = serviceControllerPerson.flood(stations);

		List<FloodPerson> floodPersons = floods.get("951 LoneTree Rd");
		assertThat(floodPersons.get(0).getFirstName()).isEqualTo("Eric");
		assertThat(floodPersons.get(0).getLastName()).isEqualTo("Cadigan");
		assertThat(floodPersons.get(0).getPhone()).isEqualTo("841-874-7458");
		assertThat(floodPersons.get(0).getAge()).isEqualTo(78);

	}

//	6ème Test
	@Test
	void personInfo() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(medicalRecordRepository.getMedicalRecords()).thenReturn(jsonData.getMedicalrecords());

		List<PersonInfo> personInfos = serviceControllerPerson.personInfo("John", "Boyd");

		assertThat(personInfos.get(0).getFirstName()).isEqualTo("John");
		assertThat(personInfos.get(0).getLastName()).isEqualTo("Boyd");
		assertThat(personInfos.get(0).getAddress()).isEqualTo("1509 Culver St");
		assertThat(personInfos.get(0).getEmail()).isEqualTo("jaboyd@email.com");
	}

//	7ème Test
	@Test
	void communityEmail() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		
		List<String> emails = serviceControllerPerson.communityEmail("Culver");

		assertThat(emails.get(0)).isEqualTo("jaboyd@email.com");

	}
}
