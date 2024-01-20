package com.safetynet.applisafety.model;

import java.util.List;

public class Fire {
	private int station;
	private List<FirePerson> firePersons;

	public int getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public List<FirePerson> getFirePersons() {
		return firePersons;
	}

	public void setFirePersons(List<FirePerson> firePersons) {
		this.firePersons = firePersons;
	}
}
