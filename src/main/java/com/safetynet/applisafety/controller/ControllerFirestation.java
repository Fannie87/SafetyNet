package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.config.ServiceJSON;
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.JsonData;

@RestController
public class ControllerFirestation {
	
	@Autowired
	private ServiceJSON serviceJSON;
	
	@PostMapping("/firestation")
	List<FireStation> postMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();

		database.getFirestations().add(firestationParam);
		serviceJSON.updateDatabase(database);

		return database.getFirestations();
	}

	@PutMapping("/firestation")
	List<FireStation> putMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();

		List<FireStation> fireStations = database.getFirestations();
		for (FireStation fireStation : fireStations) {
			if (fireStation.getAddress().equals(firestationParam.getAddress())) {
				fireStation.setStation(firestationParam.getStation());
			}

		}

		serviceJSON.updateDatabase(database);

		return database.getFirestations();
	}

	@DeleteMapping("/firestation")
	List<FireStation> deleteMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = serviceJSON.getJSONFile();

		List<FireStation> fireStations = database.getFirestations();

//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		fireStations.removeIf(firestation -> firestation.getAddress().equals(firestationParam.getAddress())
				|| firestation.getStation() == firestationParam.getStation());

		serviceJSON.updateDatabase(database);

		return database.getFirestations();
	}

}
