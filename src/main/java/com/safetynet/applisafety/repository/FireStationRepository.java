package com.safetynet.applisafety.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.safetynet.applisafety.model.json.FireStation;
import com.safetynet.applisafety.utils.Utils;

@Repository
public class FireStationRepository {
	
	private List<FireStation> fireStations;
	
	public void loadJSONData() throws IOException { // de-serialization
		fireStations = Utils.loadJSONData().getFirestations();
	}
	
	public List<FireStation> getFireStations() {
		return fireStations;
	}
	
	public List<FireStation> addFireStation(FireStation fireStation){
		fireStations.add(fireStation);
		return fireStations;
	}
	
	public List<FireStation> deleteFireStation(FireStation fireStation){
//		Fonction Lambda: simplifie l'écrture et pas besoin de faire plusieurs boucles pour récupérer l'index (firstname)
//		entrée -> retour de la fonction()
		fireStations.removeIf(firestation -> firestation.getAddress().equals(fireStation.getAddress())
				&& firestation.getStation() == fireStation.getStation());
		return fireStations;
	}
	
	public List<FireStation> updateFireStation(FireStation fireStationParam){
		for (FireStation fireStation : fireStations) {
			if (fireStation.getAddress().equals(fireStationParam.getAddress())) {
				fireStation.setStation(fireStationParam.getStation());
				break;
			}
		}
		return fireStations;
	}
	
}
