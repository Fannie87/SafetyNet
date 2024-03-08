package com.safetynet.applisafety.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.model.json.JsonData;
import com.safetynet.applisafety.model.json.MedicalRecord;

public class Utils {
	public static final int AGE_MAJORITE = 18;
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public static JsonData loadJSONData() throws IOException { // de-serialization
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(content, JsonData.class);
	}
	
	
	public static LocalDate calculateBirthdate(MedicalRecord medicalRecord) {
		return LocalDate.parse(medicalRecord.getBirthdate(), formatter);
	}

}
