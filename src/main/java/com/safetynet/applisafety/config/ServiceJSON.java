package com.safetynet.applisafety.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.applisafety.model.json.JsonData;
@Service
public class ServiceJSON {
	public JsonData getJSONFile() throws IOException { // de-serialization
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		JsonData database = objectMapper.readValue(content, JsonData.class);
		return database;
	}
	
	public void updateDatabase(JsonData jsonData) throws IOException { // serialization
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(Paths.get("src/main/resources/data.json").toFile(), jsonData);
	}
	
}
