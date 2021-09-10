package com.robertene.superheroes.config.security.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.robertene.superheroes.common.HashHelper;

public class SHA256PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return HashHelper.hashSHA256(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
