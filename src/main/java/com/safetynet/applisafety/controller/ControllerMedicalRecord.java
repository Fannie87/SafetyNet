package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.utils.ServiceJSON;

@RestController
public class ControllerMedicalRecord {

	@PostMapping("/medicalRecord")
	List<MedicalRecord> postMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();

		database.getMedicalrecords().add(medicalRecordParam);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getMedicalrecords();
	}

	@PutMapping("/medicalRecord")
	List<MedicalRecord> putMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();

		List<MedicalRecord> medicalRecords = database.getMedicalrecords();
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())&&
			medicalRecord.getLastName().equals(medicalRecordParam.getLastName())){
				medicalRecord.setAllergies(medicalRecordParam.getAllergies());
				medicalRecord.setBirthdate(medicalRecordParam.getBirthdate());
				medicalRecord.setMedications(medicalRecordParam.getMedications());
			}
		
		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getMedicalrecords();
	}

	@DeleteMapping("/medicalRecord")
	List<MedicalRecord> deleteMapping(@RequestBody MedicalRecord medicalRecordParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();
		
		List<MedicalRecord> medicalRecords = database.getMedicalrecords();

//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
				&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName()));

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getMedicalrecords();
	}

}
