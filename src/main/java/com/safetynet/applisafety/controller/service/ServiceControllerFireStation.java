package com.safetynet.applisafety.controller.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.applisafety.controller.repository.FireStationRepository;
import com.safetynet.applisafety.model.json.FireStation;

@Service
public class ServiceControllerFireStation {

	@Autowired
	private FireStationRepository fireStationRepository;

	public List<FireStation> addFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.addFireStation(fireStationParam);
	}

	public List<FireStation> updateFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.updateFireStation(fireStationParam);
	}

	public List<FireStation> deleteFireStation(FireStation fireStationParam) throws IOException {
		return fireStationRepository.deleteFireStation(fireStationParam);
	}

}
