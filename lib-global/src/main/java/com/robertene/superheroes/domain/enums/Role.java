package com.robertene.superheroes.domain.enums;

public enum Role {

	ADMIN("ADMIN"), USER("USER");

	private String type;

	Role(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static Role fromString(String type) {
		for (Role r : Role.values()) {
			if (r.getType().equalsIgnoreCase(type)) {
				return r;
			}
		}
		return null;
	}

	public static String stringValues() {
		StringBuilder sb = new StringBuilder("[");

		for (Role r : Role.values()) {
			sb.append(r.getType());
			if (!r.equals(Role.values()[Role.values().length - 1]))
				sb.append(", ");
		}
		return sb.append("]").toString();
	}
}
