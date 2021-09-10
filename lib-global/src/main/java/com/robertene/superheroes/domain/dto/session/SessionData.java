package com.robertene.superheroes.domain.dto.session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.robertene.superheroes.domain.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = { "userId", "username", "role", "email" })
@AllArgsConstructor(staticName = "of")
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = SessionData.Builder.class)
public class SessionData {

	public static final String KEY = "session_data";

	private Long userId;
	private String username;
	private Role role;
	private String email;

	@JsonPOJOBuilder(withPrefix = "")
	public static class Builder {
	}
}