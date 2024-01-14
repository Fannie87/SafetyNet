package com.safetynet.applisafety.model;

import java.util.List;

public class FireStationWithCountdown {
	private int numberMinor;
	private int numberAdult;
	private List<Person> personsFiltered;

	public int getNumberMinor() {
		return numberMinor;
	}

	public void setNumberMinor(int numberMinor) {
		this.numberMinor = numberMinor;
	}

	public int getNumberAdult() {
		return numberAdult;
	}

	public void setNumberAdult(int numberAdult) {
		this.numberAdult = numberAdult;
	}

	public List<Person> getPersonsFiltered() {
		return personsFiltered;
	}

	public void setPersonsFiltered(List<Person> personsFiltered) {
		this.personsFiltered = personsFiltered;
	}

}
