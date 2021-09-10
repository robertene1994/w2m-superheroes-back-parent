package com.robertene.superheroes.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.experimental.UtilityClass;

/**
 * Clase que contiene difrentes métodos de utilidad para gestionar datos en
 * formato JSON.
 * 
 * @author Robert Ene
 *
 */
@UtilityClass
public class JsonUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

	private static final String ERROR_JSON = "Error formateando los datos JSON a partir del objeto: {} ";
	private static final Pattern CAMEL_2_SNAKE_PATTERN = Pattern.compile("(?<!^)(?=[A-Z])");

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final ObjectMapper MAPPER_PRETTY;

	static {
		MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
		MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		MAPPER.registerModule(new JavaTimeModule());
		MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		MAPPER_PRETTY = MAPPER.copy();
		MAPPER_PRETTY.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public static Map<String, Object> toMap(JsonNode jsonNode) {
		return MAPPER.convertValue(jsonNode, new TypeReference<Map<String, Object>>() { });
	}

	public static Map<String, Object> toMap(String json) throws JsonProcessingException {
		return MAPPER.convertValue(MAPPER.readTree(json), new TypeReference<Map<String, Object>>() { });
	}

	public static <T> T toList(String json, Class<?> klass) throws IOException {
		return parseJSON(json, MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, klass));
	}

	public static String toPrettyJSON(Object obj, Class<?> klass) throws IOException {
		if (obj == null) {
			return null;
		}
		return MAPPER_PRETTY.writerFor(klass).writeValueAsString(obj);
	}

	public static String toPrettyJSON(Object obj) throws IOException {
		if (obj == null) {
			return null;
		}
		return MAPPER_PRETTY.writeValueAsString(obj);
	}

	public static <T> T parseJSON(String json, final Class<T> type) throws IOException {
		return parseJSON(json, MAPPER.getTypeFactory().constructType(type));
	}

	public static <T> T parseJSON(String json, TypeReference<T> type) throws IOException {
		return parseJSON(json, MAPPER.getTypeFactory().constructType(type));
	}

	public static <T> T parseJSON(String json, JavaType type) throws IOException {
		try {
			if (json == null) {
				return null;
			}
			return MAPPER.readValue(json, type);
		} catch (JsonParseException e) {
			LOG.error(ERROR_JSON, json, e);
			if (json.length() > 60) {
				json = json.substring(0, 50) + "...";
			}
			throw new IOException(ERROR_JSON + json, e);
		}
	}

	public static String toJSON(Object obj) throws IOException {
		if (obj == null) {
			return null;
		}
		return MAPPER.writeValueAsString(obj);
	}

	public static String toSnakeCase(String name) {
		return CAMEL_2_SNAKE_PATTERN.matcher(name).replaceAll("_").toLowerCase();
	}
}
