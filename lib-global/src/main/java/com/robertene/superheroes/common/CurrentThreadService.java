package com.robertene.superheroes.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Clase que representa el servicio que permite gestionar y almacenar datos de
 * forma común al sistema.
 * 
 * @author Robert Ene
 *
 */
@Service
public class CurrentThreadService {

	private final InheritableThreadLocal<Map<String, Object>> threadData = new InheritableThreadLocal<>();

	public Map<String, Object> getData() {
		Map<String, Object> data = threadData.get();
		if (data == null) {
			data = new HashMap<>();
			threadData.set(data);
		}
		return data;
	}

	public void clean() {
		threadData.remove();
	}

	public <T> T get(String key, Class<T> klass) {
		return klass.cast(getData().get(key));
	}

	public Object get(String key) {
		return getData().get(key);
	}

	public void set(String key, Object value) {
		getData().put(key, value);
	}
}
