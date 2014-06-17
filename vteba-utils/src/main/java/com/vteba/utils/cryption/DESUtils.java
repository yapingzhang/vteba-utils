package com.vteba.utils.cryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES对称加密和解密方法
 * 
 * @author yinlei date 2012-7-16 下午4:54:51
 */
public class DESUtils {
	private static Key key;
	private static String ENCRYPTION_KEY = "sKmBWVteFlFyiNLeI";

	static {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(ENCRYPTION_KEY.getBytes());
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(random);// new SecureRandom(ENCRYPTION_KEY.getBytes()));
			key = generator.generateKey();
			generator = null;
			//key = generateKey(ENCRYPTION_KEY);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得密钥
	 * 
	 * @param secretKey 加密盐salt
	 * @return SecretKey密钥实例
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 */
	protected static SecretKey generateKey(String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException,
			InvalidKeySpecException {

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
		keyFactory.generateSecret(keySpec);
		return keyFactory.generateSecret(keySpec);
	}

	/**
	 * 对字符串进行DES加密，并返回BASE64加密的字符创
	 * @param original源字符
	 * @return 加密后的字符
	 * @author yinlei date 2012-7-16 下午4:55:30
	 */
	public static String getEncryptString(String original) {
		try {
			byte[] strBytes = original.getBytes();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptStrByte = cipher.doFinal(strBytes);
			return CryptionUtils.base64Encode(encryptStrByte);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对DES加密和BASE64加密后的字符解密
	 * @param encryptStr 加密过的字符
	 * @return 解密后的原始字符
	 * @author yinlei date 2012-7-16 下午4:56:45
	 */
	public static String getDecryptString(String encryptStr) {
		try {
			byte[] strBytes = CryptionUtils.base64Decode(encryptStr);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decryptStrByte = cipher.doFinal(strBytes);
			return new String(decryptStrByte, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		String str = "root";
		String pass = "0558yinlei3416763";
		String enstr = getEncryptString(str);
		String enpass = getEncryptString(pass);
		System.out.println(enstr);
		System.out.println(enpass);
		System.out.println(getDecryptString(enstr));
		System.out.println(getDecryptString(enpass));
	}
}