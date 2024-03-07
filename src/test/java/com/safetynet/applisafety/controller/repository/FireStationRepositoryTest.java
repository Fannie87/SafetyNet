package com.safetynet.applisafety.controller.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.safetynet.applisafety.model.json.FireStation;

@ExtendWith(MockitoExtension.class)
public class FireStationRepositoryTest {

	@InjectMocks
	private FireStationRepository fireStationRepository;

	@Mock
	private List<FireStation> fireStations;
	
	@Test
	void addFireStation() throws Exception {
		FireStation fireStation = new FireStation();
		
		fireStationRepository.addFireStation(fireStation);
		
		verify(fireStations).add(fireStation);
	}

	
	@Test
	void deleteFireStation() throws Exception {
		FireStation fireStation = new FireStation();
		
		fireStationRepository.deleteFireStation(fireStation);
		
		verify(fireStations).removeIf(any());
	}

	@Test
	void updateFireStation() throws Exception {
		FireStation fireStation = new FireStation();
		
		fireStationRepository.updateFireStation(fireStation);
		
		verify(fireStations).removeIf(any());
	}
}
