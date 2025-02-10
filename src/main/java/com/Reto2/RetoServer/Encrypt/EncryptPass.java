package com.Reto2.RetoServer.Encrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptPass {

	private static String KEY_ALGORITHM = "AES";
	private static String CHARSET_NAME = "utf-8";

	public static String AESEncode(String encodeKey, String content) {
		try {
		
			KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
		
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeKey.getBytes());
			keygen.init(128, secureRandom);
			
			SecretKey original_key = keygen.generateKey();
			
			byte[] raw = original_key.getEncoded();
		
			SecretKey key = new SecretKeySpec(raw, KEY_ALGORITHM);
			
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			byte[] byte_encode = content.getBytes(CHARSET_NAME);
		
			byte[] byte_AES = cipher.doFinal(byte_encode);
			String AES_encode = new String(Base64.getEncoder().encodeToString(byte_AES));
			
			return AES_encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String AESDncode(String encodeKey, String content) {
		try {

			KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeKey.getBytes());
			keygen.init(128, secureRandom);

			SecretKey original_key = keygen.generateKey();

			byte[] raw = original_key.getEncoded();

			SecretKey key = new SecretKeySpec(raw, KEY_ALGORITHM);

			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] byte_content = Base64.getDecoder().decode(content);

			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, CHARSET_NAME);
			return AES_decode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
