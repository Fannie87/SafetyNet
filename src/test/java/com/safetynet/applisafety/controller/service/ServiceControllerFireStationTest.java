package com.safetynet.applisafety.controller.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.json.JsonData;
@ExtendWith(MockitoExtension.class)
public class ServiceControllerFireStationTest {

	@InjectMocks
	private ServiceControllerFireStation serviceControllerFireStation;
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

	@Test
	void fireStations() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(fireStationRepository.getFireStations()).thenReturn(jsonData.getFirestations());
		when(medicalRecordRepository.getMedicalRecords()).thenReturn(jsonData.getMedicalrecords());

		FireStationWithCountdown fireStationWithCountdowns = serviceControllerFireStation.fireStations(1);

		assertThat(fireStationWithCountdowns.getNumberMinor()).isEqualTo(1);
		assertThat(fireStationWithCountdowns.getNumberAdult()).isEqualTo(5);
	}


	@Test
	void phoneAlert() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(fireStationRepository.getFireStations()).thenReturn(jsonData.getFirestations());
		
		List<String> phones = serviceControllerFireStation.phoneAlert(1);

		assertThat(phones.get(0)).isEqualTo("841-874-6512");
		assertThat(phones.get(1)).isEqualTo("841-874-8547");
	}

//	4ème Test
	@Test
	void fire() throws IOException {
		when(personRepository.getPersons()).thenReturn(jsonData.getPersons());
		when(fireStationRepository.getFireStations()).thenReturn(jsonData.getFirestations());
		when(medicalRecordRepository.getMedicalRecords()).thenReturn(jsonData.getMedicalrecords());
		
		Fire fire = serviceControllerFireStation.fire("1509 Culver St");

		assertThat(fire.getStation()).isEqualTo(3);
		assertThat(fire.getFirePersons().get(0).getFirstName()).isEqualTo("Roger");
		assertThat(fire.getFirePersons().get(0).getLastName()).isEqualTo("Boyd");
		assertThat(fire.getFirePersons().get(0).getPhone()).isEqualTo("841-874-6512");
		assertThat(fire.getFirePersons().get(0).getAge()).isEqualTo(6);
	}


}
