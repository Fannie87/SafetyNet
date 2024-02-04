package com.safetynet.applisafety.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;
import com.safetynet.applisafety.model.json.JsonData;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
	@InjectMocks
	private Controller controller;

	@Mock
	private ServiceJSON serviceJSON;

	static JsonData jsonData;

	@BeforeAll
	public static void beforeAll() throws IOException {
		String content = Files.readString(Paths.get("src/test/resources/data-test.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		jsonData = objectMapper.readValue(content, JsonData.class);
	}

	@Test
	void fireStations() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		FireStationWithCountdown fireStationWithCountdowns = controller.fireStations(1);

		assertThat(fireStationWithCountdowns.getNumberMinor()).isEqualTo(1);
		assertThat(fireStationWithCountdowns.getNumberAdult()).isEqualTo(5);
	}

	@Test
	void childAlert() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<ChildAlert> childAlerts = controller.childAlert("1509 Culver St");

		assertThat(childAlerts.get(0).getFirstName()).isEqualTo("Tenley");
		assertThat(childAlerts.get(0).getLastName()).isEqualTo("Boyd");
		assertThat(childAlerts.get(0).getAge()).isEqualTo(11);
	}

	@Test
	void phoneAlert() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<String> phones = controller.phoneAlert(1);

		assertThat(phones.get(0)).isEqualTo("841-874-6512");
		assertThat(phones.get(1)).isEqualTo("841-874-8547");
	}

//	4ème Test
	@Test
	void fire() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		Fire fire = controller.fire("1509 Culver St");

		assertThat(fire.getStation()).isEqualTo(3);
		assertThat(fire.getFirePersons().get(0).getFirstName()).isEqualTo("John");
		assertThat(fire.getFirePersons().get(0).getLastName()).isEqualTo("Boyd");
		assertThat(fire.getFirePersons().get(0).getPhone()).isEqualTo("841-874-6512");
		assertThat(fire.getFirePersons().get(0).getAge()).isEqualTo(39);
	}

	// 5ème Test
	@Test
	void flood() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<Integer> stations = new ArrayList<Integer>();
		stations.add(1);
		stations.add(2);
		Map<String, List<FloodPerson>> floods = controller.flood(stations);

		List<FloodPerson> floodPersons = floods.get("951 LoneTree Rd");
		assertThat(floodPersons.get(0).getFirstName()).isEqualTo("Eric");
		assertThat(floodPersons.get(0).getLastName()).isEqualTo("Cadigan");
		assertThat(floodPersons.get(0).getPhone()).isEqualTo("841-874-7458");
		assertThat(floodPersons.get(0).getAge()).isEqualTo(78);

	}

//	6ème Test
	@Test
	void personInfo() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<PersonInfo> personInfos = controller.personInfo("John", "Boyd");

		assertThat(personInfos.get(0).getFirstName()).isEqualTo("John");
		assertThat(personInfos.get(0).getLastName()).isEqualTo("Boyd");
		assertThat(personInfos.get(0).getAddress()).isEqualTo("1509 Culver St");
		assertThat(personInfos.get(0).getEmail()).isEqualTo("jaboyd@email.com");
	}
	
//	7ème Test
	@Test
	void communityEmail() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);
		
		List<String> emails = controller.communityEmail("Culver");
		
		assertThat(emails.get(0)).isEqualTo("jaboyd@email.com");
		
	}
}
