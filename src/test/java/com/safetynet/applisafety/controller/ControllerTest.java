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
import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.FireStationWithCountdown;
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
	void fireStations() throws Exception {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		FireStationWithCountdown fireStationWithCountdowns = controller.fireStations(1);

		assertThat(fireStationWithCountdowns.getNumberMinor()).isEqualTo(1);
		assertThat(fireStationWithCountdowns.getNumberAdult()).isEqualTo(5);
	}

	@Test
	private void childAlert() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<ChildAlert> childAlerts = controller.childAlert("1509 Culver St");

		assertThat(childAlerts.get(0).getFirstName()).isEqualTo("Tenley");
		assertThat(childAlerts.get(0).getLastName()).isEqualTo("Boyd");
		assertThat(childAlerts.get(0).getAge()).isEqualTo(11);
	}

	@Test
	private void phoneAlert() throws IOException {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		List<String> phones = controller.phoneAlert(1);

		assertThat(phones.get(0)).isEqualTo("841-874-6512");
		assertThat(phones.get(1)).isEqualTo("841-874-8547");
	}


//	4ème Test
//	@Test
//	private void 
 }
