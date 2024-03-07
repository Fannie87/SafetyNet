package com.safetynet.applisafety.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.applisafety.model.json.JsonData;

public class Utils {
	public static JsonData loadJSONData() throws IOException { // de-serialization
		String content = Files.readString(Paths.get("src/main/resources/data.json"), Charset.defaultCharset());
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(content, JsonData.class);
	}
}
