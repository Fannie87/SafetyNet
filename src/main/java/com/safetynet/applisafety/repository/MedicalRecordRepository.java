package com.safetynet.applisafety.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.safetynet.applisafety.model.json.MedicalRecord;
import com.safetynet.applisafety.utils.Utils;

@Repository
public class MedicalRecordRepository {

	private List<MedicalRecord> medicalRecords;

	public void loadJSONData() throws IOException {
		medicalRecords = Utils.loadJSONData().getMedicalrecords();
	}

	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}

	public List<MedicalRecord> addMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
					&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
						"Ce dossier médical a déjà été ajouté. Vous ne pouvez pas le saisir à nouveau.");
			}
		}
		medicalRecords.add(medicalRecordParam);
		return medicalRecords;
	}

	public List<MedicalRecord> deleteMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
				&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName()));
		return medicalRecords;
	}

	public List<MedicalRecord> updateMedicalRecord(MedicalRecord medicalRecordParam) throws IOException {
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(medicalRecordParam.getFirstName())
					&& medicalRecord.getLastName().equals(medicalRecordParam.getLastName())) {
				medicalRecord.setAllergies(medicalRecordParam.getAllergies());
				medicalRecord.setBirthdate(medicalRecordParam.getBirthdate());
				medicalRecord.setMedications(medicalRecordParam.getMedications());
			}
		}
		return medicalRecords;
	}

}
