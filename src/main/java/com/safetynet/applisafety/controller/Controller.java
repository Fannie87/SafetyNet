package com.safetynet.applisafety.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.applisafety.controller.service.ServiceController;
import com.safetynet.applisafety.model.ChildAlert;
import com.safetynet.applisafety.model.Fire;
import com.safetynet.applisafety.model.FireStationWithCountdown;
import com.safetynet.applisafety.model.FloodPerson;
import com.safetynet.applisafety.model.PersonInfo;

@RestController
public class Controller {

	@Autowired
	private ServiceController controllerService;
	
	@GetMapping("/firestation")
	public FireStationWithCountdown fireStations(@RequestParam @NonNull Integer stationNumber) throws IOException {
		return controllerService.fireStations(stationNumber);
	}

	@GetMapping("/childAlert")
	public List<ChildAlert> childAlert(@RequestParam @NonNull String addressParam) throws IOException {
		return controllerService.childAlert(addressParam);
	}

	@GetMapping("/phoneAlert")
	public List<String> phoneAlert(@RequestParam @NonNull Integer firestationNumber) throws IOException {
		return controllerService.phoneAlert(firestationNumber);
	}

	@GetMapping("/fire")
	public Fire fire(@RequestParam @NonNull String address) throws IOException {
		return controllerService.fire(address);
	}

	@GetMapping("/flood/stations")
	public Map<String, List<FloodPerson>> flood(@RequestParam @NonNull List<Integer> stations) throws IOException {
		return controllerService.flood(stations);
	}

	@GetMapping("/personInfo")
	public List<PersonInfo> personInfo(@RequestParam @NonNull String firstName, @RequestParam @NonNull String lastName)
			throws IOException {
		return controllerService.personInfo(firstName, lastName);
	}

	@GetMapping("/communityEmail")
	public List<String> communityEmail(@RequestParam @NonNull String city) throws IOException {
		return controllerService.communityEmail(city);
	}

}
