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
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.MedicalRecord;

@ExtendWith(MockitoExtension.class)
public class ControllerMedicalRecordTest {
	@InjectMocks
	private ControllerMedicalRecord controllerMedicalRecord;

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
	void postMapping() throws Exception {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		MedicalRecord medicalRecordParam = new MedicalRecord();

		medicalRecordParam.setFirstName("John");
		medicalRecordParam.setLastName("Boyd");
		medicalRecordParam.setBirthdate("03/06/1984");

		List<MedicalRecord> listMedicalRecords = controllerMedicalRecord.addMedicalRecord(medicalRecordParam);

		assertThat(listMedicalRecords).contains(medicalRecordParam);

	}

	@Test
	void putMapping() throws Exception {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		MedicalRecord medicalRecordParam = new MedicalRecord();

		medicalRecordParam.setFirstName("John");
		medicalRecordParam.setLastName("Boyd");
		medicalRecordParam.setBirthdate("03/06/1984");

		List<MedicalRecord> listMedicalRecord = controllerMedicalRecord.updateMedicalRecord(medicalRecordParam);

		for (MedicalRecord medicalRecord : listMedicalRecord) {
			if (medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
					&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName())) {

				assertThat(medicalRecord.getBirthdate()).isEqualTo("03/06/1984");

			}
		}

	}

	@Test
	void deleteMapping() throws Exception {
		when(serviceJSON.getJSONFile()).thenReturn(jsonData);

		MedicalRecord medicalRecordParam = new MedicalRecord();

		medicalRecordParam.setFirstName("John");
		medicalRecordParam.setLastName("Boyd");
		medicalRecordParam.setBirthdate("03/06/1984");

		List<MedicalRecord> listMedicalRecord = controllerMedicalRecord.deleteMedicalRecord(medicalRecordParam);
	

				assertThat(listMedicalRecord).doesNotContain(medicalRecordParam);			
		
	}
}