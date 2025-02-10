package com.Reto2.RetoServer.Encrypt;

import java.security.MessageDigest;

import java.util.Base64;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptPass {

	private static String KEY_ALGORITHM = "AES";
	private static String CHARSET_NAME = "utf-8";

	public static String AESEncode(String encodeKey, String content) {
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(encodeKey.getBytes(CHARSET_NAME));

			SecretKey key = new SecretKeySpec(hash, KEY_ALGORITHM);

			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] byteEncode = content.getBytes(CHARSET_NAME);
			byte[] byteAES = cipher.doFinal(byteEncode);
			String AES_encode = Base64.getEncoder().encodeToString(byteAES);

			return AES_encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String AESDecode(String encodeKey, String encryptedContent) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(encodeKey.getBytes(CHARSET_NAME));

			SecretKey key = new SecretKeySpec(hash, KEY_ALGORITHM);

			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] encryptedBytes = Base64.getDecoder().decode(encryptedContent);

			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return new String(decryptedBytes, CHARSET_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
