package com.safetynet.applisafety.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public CustomLocalDateTimeSerializer() {
		this(null);
	}

	public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
		super(t);
	}

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {

		gen.writeString(formatter.format(value));
	}
}