package com.safetynet.applisafety.controller.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.applisafety.model.json.FireStation;

@ExtendWith(MockitoExtension.class)
public class FireStationRepositoryTest {

	@InjectMocks
	private FireStationRepository fireStationRepository;

	@Test
	void addFireStation() throws Exception {
		fireStationRepository.loadJSONData();
		FireStation fireStation = new FireStation();

		List<FireStation> fireStations = fireStationRepository.addFireStation(fireStation);

		assertThat(fireStations.size()).isEqualTo(14);
	}

	@Test
	void deleteFireStation() throws Exception {
		fireStationRepository.loadJSONData();
		FireStation fireStation = new FireStation();
		fireStation.setAddress("1509 Culver St");
		fireStation.setStation(3);

		List<FireStation> fireStations = fireStationRepository.deleteFireStation(fireStation);

		assertThat(fireStations.size()).isEqualTo(12);
	}

	@Test
	void updateFireStation() throws Exception {
		fireStationRepository.loadJSONData();
		FireStation fireStation = new FireStation();
		fireStation.setAddress("1509 Culver St");
		fireStation.setStation(4);

		List<FireStation> fireStations = fireStationRepository.updateFireStation(fireStation);

		assertThat(fireStations.get(0).getStation()).isEqualTo(4);
	}
}
