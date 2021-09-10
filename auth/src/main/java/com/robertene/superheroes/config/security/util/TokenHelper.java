package com.robertene.superheroes.config.security.util;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.robertene.superheroes.common.JsonUtils;
import com.robertene.superheroes.domain.dto.session.SessionData;
import com.robertene.superheroes.domain.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

@Service
public class TokenHelper {

	private static final Logger LOG = LoggerFactory.getLogger(TokenHelper.class);
	private static final long DEFAULT_SESSION_DURATION_H = 15;
	private static final String ERROR_TOKEN = "ERROR Validando el token: ";

	@Value("${jwt.secret}")
	private String secret;

	public String generate(SessionData tokenData) {
		return generate(tokenData, DEFAULT_SESSION_DURATION_H);
	}

	public String generate(SessionData tokenData, long expirationInHours) {
		return Jwts.builder().claim(SessionData.KEY, tokenData).setIssuedAt(Date.from(Instant.now()))
				.setExpiration(Date.from(Instant.now().plus(expirationInHours, ChronoUnit.HOURS)))
				.signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(secret)).compact();

	}

	public boolean validate(String token) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(secret)).parseClaimsJws(token);
			if (jws != null) {
				Claims tokenData = jws.getBody();
				return tokenData.getExpiration().after(new Date());
			}
		} catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException ex) {
			// Ignored
		}
		return false;
	}

	public SessionData getSessionData(String token) throws InvalidTokenException {
		return getTokenData(token, SessionData.KEY, SessionData.class);
	}

	public <T> T getTokenData(String token, String key, Class<T> klass) throws InvalidTokenException {
		try {

			Jws<Claims> jws = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(secret)).parseClaimsJws(token);
			String rawValue = JsonUtils.toJSON(jws.getBody().get(key));
			return JsonUtils.parseJSON(rawValue, klass);

		} catch (IOException | ExpiredJwtException | MalformedJwtException | IllegalArgumentException ex) {
			LOG.error("{} {}", ERROR_TOKEN, ex.toString());
			throw new InvalidTokenException();
		}
	}

	public Long getUserId(String tokenHeader) {
		Long userId = null;
		try {
			if (tokenHeader != null) {
				String token = tokenHeader.substring("Bearer ".length()).trim();
				userId = this.getSessionData(token).getUserId();
			}
		} catch (InvalidTokenException ex) {
			LOG.error("{} {}", ERROR_TOKEN, ex.toString());
		}

		return userId;
	}
}
