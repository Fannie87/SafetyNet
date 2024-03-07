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
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.JsonData;

@ExtendWith(MockitoExtension.class)
public class ControllerFirestationTest {
	@InjectMocks
	private ControllerFireStation controllerFirestation;

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

				
		FireStation firestationParam = new FireStation();

		firestationParam.setAddress("748 Calmette couvin Dr");
		firestationParam.setStation(99);

		List<FireStation> listFirestation = controllerFirestation.addFirestation(firestationParam);
		
		assertThat(listFirestation).contains(firestationParam);
			
	}
	
	@Test
	void putMapping() throws Exception {
		when(serviceJSON.loadJSONData()).thenReturn(jsonData);

				
		FireStation firestationParam = new FireStation();

		firestationParam.setAddress("748 Calmette couvin Dr");
		firestationParam.setStation(100);

		List<FireStation> listFirestation = controllerFirestation.updateFirestation(firestationParam);
		
		for (FireStation fireStation : listFirestation) {
			if (fireStation.getAddress().equals(firestationParam.getAddress())){
				assertThat(fireStation.getAddress()).isEqualTo("748 Calmette couvin Dr");
				assertThat(fireStation.getStation()).isEqualTo(100);
			}
		}
				
	}
	
	@Test
	void deleteMapping() throws Exception {
		when(serviceJSON.loadJSONData()).thenReturn(jsonData);

				
		FireStation firestationParam = new FireStation();

		firestationParam.setAddress("748 Calmette couvin Dr");
		firestationParam.setStation(99);

		List<FireStation> listFirestation = controllerFirestation.deleteFirestation(firestationParam);
		
		assertThat(listFirestation).doesNotContain(firestationParam);
			
	}

}
