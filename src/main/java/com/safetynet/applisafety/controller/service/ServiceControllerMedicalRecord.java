package com.safetynet.applisafety.controller.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.applisafety.controller.repository.MedicalRecordRepository;
import com.safetynet.applisafety.model.json.MedicalRecord;

@Service
public class ServiceControllerMedicalRecord {
	@Autowired
	private MedicalRecordRepository medicalRecordRepository;
	
	public List<MedicalRecord> addMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
		return medicalRecordRepository.addMedicalRecord(medicalRecordParam);
	}
	
	public List<MedicalRecord> updateMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
		return medicalRecordRepository.updateMedicalRecord(medicalRecordParam);
	}
	
	public List<MedicalRecord> deleteMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
		return medicalRecordRepository.deleteMedicalRecord(medicalRecordParam);
	}

}
