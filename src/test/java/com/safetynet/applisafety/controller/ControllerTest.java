package com.safetynet.applisafety.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.json.JsonData;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
	@InjectMocks
	Controller controller;

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

		FireStationWithCountdown fireStations = controller.fireStations(1);
		
		
		//assert
		assertThat(fireStations).isNotNull();
	}

//	@Test
//	private void childAlert() throws Exception{
//		JsonData titi = new JsonData();
//		List<ChildAlert> childAlerts = new ArrayList<>();
//		ChildAlert childAlert = new ChildAlert();
//		childAlert.setFirstName("123 soleil");
//		childAlert.setLastName("Emile");
//		childAlert.setAge(12);
//		childAlerts.add(childAlert);
//		titi.setPersons(childAlerts);
//		titi.setMedicalrecords(childAlerts);
//	}
}
