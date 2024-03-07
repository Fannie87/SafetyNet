package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.controller.service.ServiceControllerFireStation;
import com.safetynet.applisafety.model.json.FireStation;

@RestController
public class ControllerFireStation {
	

	@Autowired
	private ServiceControllerFireStation serviceControllerFireStation;

	@PostMapping("/firestation")
	List<FireStation> addFirestation(@RequestBody FireStation fireStationParam) throws IOException {
		return serviceControllerFireStation.addFireStation(fireStationParam);
	}

	@PutMapping("/firestation")
	List<FireStation> updateFirestation(@RequestBody FireStation fireStationParam) throws IOException {
		return serviceControllerFireStation.updateFireStation(fireStationParam);
	}

	@DeleteMapping("/firestation")
	List<FireStation> deleteFirestation(@RequestBody FireStation fireStationParam) throws IOException {
		return serviceControllerFireStation.deleteFireStation(fireStationParam);
	}

}
