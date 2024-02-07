package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.MedicalRecord;

@RestController
public class ControllerMedicalRecord {
	
	private static final Logger logger = LogManager.getLogger(Controller.class);;


	@Autowired
	private ServiceJSON serviceJSON;
	
	@PostMapping("/medicalRecord")
	List<MedicalRecord> postMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		
		logger.info("/medicalRecord, parametres : medicalRecordParam=" + medicalRecordParam);
		
		JsonData database = serviceJSON.getJSONFile();

		database.getMedicalrecords().add(medicalRecordParam);
		serviceJSON.updateDatabase(database);
		
		logger.info("/medicalRecord, retour : " + new ObjectMapper().writeValueAsString(database.getMedicalrecords()));
		return database.getMedicalrecords();
	}

	@PutMapping("/medicalRecord")
	List<MedicalRecord> putMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		
		logger.info("/medicalRecord, parametres : medicalRecordParam=" + medicalRecordParam);
		
		JsonData database = serviceJSON.getJSONFile();

		List<MedicalRecord> medicalRecords = database.getMedicalrecords();
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())&&
			medicalRecord.getLastName().equals(medicalRecordParam.getLastName())){
				medicalRecord.setAllergies(medicalRecordParam.getAllergies());
				medicalRecord.setBirthdate(medicalRecordParam.getBirthdate());
				medicalRecord.setMedications(medicalRecordParam.getMedications());
			}
		
		}

		serviceJSON.updateDatabase(database);

		logger.info("/medicalRecord, retour : " + new ObjectMapper().writeValueAsString(database.getMedicalrecords()));
		return database.getMedicalrecords();
	}

	@DeleteMapping("/medicalRecord")
	List<MedicalRecord> deleteMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		
		logger.info("/medicalRecord, parametres : medicalRecordParam=" + medicalRecordParam);
		
		JsonData database = serviceJSON.getJSONFile();
		
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
				&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName()));
		
// Evite les copier/coller : classe ServiceJSON
		serviceJSON.updateDatabase(database);
		
		logger.info("/medicalRecord, retour : " + new ObjectMapper().writeValueAsString(database.getMedicalrecords()));
		return database.getMedicalrecords();
	}

}
