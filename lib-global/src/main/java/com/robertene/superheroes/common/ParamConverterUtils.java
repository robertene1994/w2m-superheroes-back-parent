package com.robertene.superheroes.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;

import lombok.experimental.UtilityClass;

/**
 * Clase que contiene difrentes métodos de utilidad para gestionar los
 * parámetros que llegan a los diferentes controladores.
 * 
 * @author Robert Ene
 *
 */
@UtilityClass
public class ParamConverterUtils {

	public static boolean isInteger(String value) {
		return NumberUtils.isDigits(value);
	}

	public static boolean isDecimal(String value) {
		return (!isInteger(value) && NumberUtils.isCreatable(value));
	}

	public static boolean isBlankOrNull(String value) {
		return GenericValidator.isBlankOrNull(value);
	}

	public static String isValueBlank(String value) {
		if (isBlankOrNull(value) && value != null && !value.isEmpty()) {
			value = ObjectUtils.identityToString(value);
		}
		return value;
	}

	public static boolean isLocalDate(String value) {
		return GenericValidator.isDate(value, Locale.getDefault());
	}

	public static boolean isLocalDateTimeIsoJava(String value) {
		return GenericValidator.isDate(value, "yyyy-MM-dd'T'HH:mm:ss", false);
	}

	public static boolean isDateTypeFormatterIsoJavascript(String value) {
		return GenericValidator.isDate(value, "yyyy-MM-dd'T'HH:mm:ss.SSSX", false);
	}

	public static List<Object> verifyConvertTypeValueList(List<String> paramValuesOfKey) {
		List<Object> listObject = new ArrayList<>();
		if (isAListInteger(paramValuesOfKey)) {
			for (Object value : paramValuesOfKey) {
				listObject.add(NumberUtils.createInteger(value.toString()));
			}
		} else if (isAListCharacter(paramValuesOfKey)) {
			for (Object value : paramValuesOfKey) {
				listObject.add(value.toString());
			}
		}
		return listObject;
	}

	private static boolean isAListInteger(List<String> paramValuesOfKey) {
		for (String value : paramValuesOfKey) {
			if (!isInteger(value)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isAListCharacter(List<String> paramValuesOfKey) {
		for (String value : paramValuesOfKey) {
			if (!isCharacter(value)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isCharacter(String value) {
		for (int x = 0; x < value.length(); x++) {
			char c = value.charAt(x);
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ')) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBoolean(String value) {
		return ("true".equals(value) || "false".equals(value));
	}
}
