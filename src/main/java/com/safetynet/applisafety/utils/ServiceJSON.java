package com.safetynet.applisafety.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.model.json.JsonData;

public class ServiceJSON {
	public static JsonData getJSONFile() throws IOException {
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		JsonData database = objectMapper.readValue(content, JsonData.class);
		return database;
	}
	
//	public static Database updateDatabase() throws IOException {
//		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
//		ObjectMapper objectMapper = new ObjectMapper();
//		Database database = objectMapper.readValue(content, Database.class);
//		return database;
//	}
}
