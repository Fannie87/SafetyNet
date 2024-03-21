package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	@Autowired
	private MedicalRecordService serviceControllerMedicalRecord;

	@PostMapping("/medicalRecord")
	List<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		return serviceControllerMedicalRecord.addMedicalRecord(medicalRecordParam);
	}

	@PutMapping("/medicalRecord")
	List<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		return serviceControllerMedicalRecord.updateMedicalRecord(medicalRecordParam);
	}

	@DeleteMapping("/medicalRecord")
	List<MedicalRecord> deleteMedicalRecord(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		return serviceControllerMedicalRecord.deleteMedicalRecord(medicalRecordParam);
	}

}
