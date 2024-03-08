package com.safetynet.applisafety.controller.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.applisafety.model.json.MedicalRecord;

@ExtendWith(MockitoExtension.class)

public class MedicalRecordRepositoryTest {
	@InjectMocks
	private MedicalRecordRepository medicalRecordRepository;
	
	@Test
	void addMedicalRecord() throws Exception {
		medicalRecordRepository.loadJSONData();
		
		MedicalRecord medicalRecordParam = new MedicalRecord();
		List<MedicalRecord> medicalRecords = medicalRecordRepository.addMedicalRecord(medicalRecordParam);
		
		assertThat(medicalRecords.size()).isEqualTo(24);
	}
	
	@Test
	void deleteMedicalRecord() throws Exception {
		medicalRecordRepository.loadJSONData();
		MedicalRecord medicalRecord =new MedicalRecord();
		medicalRecord.setFirstName("John");
		medicalRecord.setLastName("Boyd");
		
		List<MedicalRecord> medicalRecords = medicalRecordRepository.deleteMedicalRecord(medicalRecord);
		
		assertThat(medicalRecords.size()).isEqualTo(22);
	}
	
	@Test
	void updateMedicalRecord() throws Exception {
		medicalRecordRepository.loadJSONData();
		MedicalRecord medicalRecord =new MedicalRecord();
		medicalRecord.setFirstName("John");
		medicalRecord.setLastName("Boyd");
		medicalRecord.setBirthdate("03/08/2024");
		
		List<MedicalRecord> medicalRecords = medicalRecordRepository.updateMedicalRecord(medicalRecord);

		assertThat(medicalRecords.get(0).getBirthdate()).isEqualTo("03/08/2024");

	}


}
