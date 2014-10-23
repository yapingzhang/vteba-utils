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
 * DES相关的对称加密和解密方法。包含DES DESede AES。
 * 
 * @author yinlei date 2012-7-16 下午4:54:51
 */
public class DESUtils {
	private static final Key desKey;
	private static final Key aesKey;
	private static final Key desedeKey;
	private static final String ENCRYPT_SEED = "sKmBW78VteFl@1FyiN128LeI";// 加密的种子值

	static {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(ENCRYPT_SEED.getBytes());
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(random);
			desKey = generator.generateKey();
			
			generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = new SecureRandom(ENCRYPT_SEED.getBytes());// 和上面是一样的
			generator.init(secureRandom);
			aesKey = generator.generateKey();
			
			generator = KeyGenerator.getInstance("DESede");
			generator.init(secureRandom);
			desedeKey = generator.generateKey();
			
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException("初始化对称加密算法异常。", e);
		}
	}

	/**
	 * 获得密钥。这个方法产生的密钥可能跨平台性不好。
	 * 
	 * @param secretKey 加密盐salt
	 * @return SecretKey密钥实例
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 */
	protected static SecretKey generateKey(String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
		return keyFactory.generateSecret(keySpec);
	}
	
	/**
	 * 创建密匙
	 * 
	 * @param algorithm
	 *            加密算法，可用 DES，DESede，AES，Blowfish
	 * @return SecretKey 秘密（对称）密钥
	 */
	public static SecretKey createSecretKey(String algorithm) {
		// 声明KeyGenerator对象
		KeyGenerator keygen = null;
		// 声明 密钥对象
		SecretKey deskey = null;
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(ENCRYPT_SEED.getBytes());
			// 返回生成指定算法的秘密密钥的 KeyGenerator 对象
			keygen = KeyGenerator.getInstance(algorithm);
			keygen.init(random);
			// 生成一个密钥
			deskey = keygen.generateKey();
			keygen = null;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		// 返回密匙
		return deskey;
	}
	
	/**
	 * 对字符串进行DES加密，并返回BASE64加密的字符串
	 * @param original源字符
	 * @return 加密后的字符
	 * @author yinlei date 2012-7-16 下午4:55:30
	 */
	public static String getEncrypt(String original) {
		try {
			byte[] strBytes = original.getBytes();
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, desKey, secureRandom);
			byte[] encryptStrByte = cipher.doFinal(strBytes);
			return CryptUtils.base64Encode(encryptStrByte);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对DES加密和BASE64加密后的字符解密
	 * @param encrypt 加密过的字符
	 * @return 解密后的原始字符
	 * @author yinlei date 2012-7-16 下午4:56:45
	 */
	public static String getDecrypt(String encrypt) {
		try {
			byte[] strBytes = CryptUtils.base64Decode(encrypt);
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, desKey, secureRandom);
			byte[] decryptStrByte = cipher.doFinal(strBytes);
			return new String(decryptStrByte, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对字符串进行AES加密，并返回BASE64加密的字符串
	 * @param original 待价密的数据
	 * @return 加密后的字符
	 * @author yinlei date 2012-7-16 17:35:34
	 */
	public static String aesEncrypt(byte[] original) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, secureRandom);
			byte[] encrypt = cipher.doFinal(original);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对AES加密后，并经过BASE64编码的字符解密
	 * @param encrypt 加密过的base64编码的字符串
	 * @return 解密后的原始字符，UTF-8编码
	 * @author yinlei date 2012-7-16 17:46:11
	 */
	public static String aesDecrypt(String encrypt) {
		try {
			byte[] strBytes = CryptUtils.base64Decode(encrypt);
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey, secureRandom);
			byte[] decryptStrByte = cipher.doFinal(strBytes);
			return new String(decryptStrByte, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 对字符串进行DESede加密，并返回BASE64加密的字符串
	 * @param original 待价密的数据
	 * @return 加密后的字符
	 * @author yinlei date 2012-7-16 18:32:31
	 */
	public static String desedeEncrypt(byte[] original) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, desedeKey, secureRandom);
			byte[] encrypt = cipher.doFinal(original);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对DESede加密后，并经过BASE64编码的字符解密
	 * @param encrypt 加密过的base64编码的字符串
	 * @return 解密后的原始字符，UTF-8编码
	 * @author yinlei date 2012-7-16 18:40:31
	 */
	public static String desedeDecrypt(String encrypt) {
		try {
			byte[] strBytes = CryptUtils.base64Decode(encrypt);
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.DECRYPT_MODE, desedeKey, secureRandom);
			byte[] decryptStrByte = cipher.doFinal(strBytes);
			return new String(decryptStrByte, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}