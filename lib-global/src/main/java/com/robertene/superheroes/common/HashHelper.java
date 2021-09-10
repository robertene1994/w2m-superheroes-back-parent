package com.robertene.superheroes.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene difrentes métodos de utilidad para "hashing".
 * 
 * @author Robert Ene
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HashHelper {

	private static final Logger LOG = LoggerFactory.getLogger(HashHelper.class);

	public static String hashSHA256(CharSequence text) {
		return hash(text, "SHA-256");
	}

	public static String hash(CharSequence text, String alg) {
		if (text == null) {
			return null;
		}
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(alg);
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.getMessage());
			return null;
		}
		return bytesToHex(digest.digest(text.toString().getBytes(StandardCharsets.UTF_8)));
	}

	public static String hashMD5(String text) {
		return hash(text, "MD5");
	}

	public static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
