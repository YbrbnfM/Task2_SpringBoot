package dev.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Cryptography {
	public static final String salt = "qwerty";
	public static String encryptWhithSha512(String input) {
		try {
			input = input + salt;
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
