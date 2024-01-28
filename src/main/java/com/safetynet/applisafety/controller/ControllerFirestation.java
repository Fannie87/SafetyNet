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
import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.utils.ServiceJSON;

@RestController
public class ControllerFirestation {
	@PostMapping("/firestation")
	List<FireStation> postMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();

		database.getFirestations().add(firestationParam);
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getFirestations();
	}

	@PutMapping("/firestation")
	List<FireStation> putMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();

		List<FireStation> fireStations = database.getFirestations();
		for (FireStation fireStation : fireStations) {
			if (fireStation.getAddress().equals(firestationParam.getAddress())) {
				fireStation.setStation(firestationParam.getStation());
			}

		}

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getFirestations();
	}

	@DeleteMapping("/firestation")
	List<FireStation> deleteMapping(@RequestBody FireStation firestationParam) throws IOException {
		JsonData database = ServiceJSON.getJSONFile();

		List<FireStation> fireStations = database.getFirestations();

//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		fireStations.removeIf(firestation -> firestation.getAddress().equals(firestationParam.getAddress())
				|| firestation.getStation() == firestationParam.getStation());

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), database);

		return database.getFirestations();
	}

}
