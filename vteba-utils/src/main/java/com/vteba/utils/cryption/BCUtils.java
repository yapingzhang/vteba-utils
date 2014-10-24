package com.vteba.utils.cryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * bouncycastle开源实现的jce和jca规范。
 * @author yinlei
 * @date 2014-1-1 16:40
 */
public class BCUtils {
	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	public static final String RSA = "RSA";
	
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	
	private static final String BC = "BC";
//	private static final Provider BC;
//	static {
//		// 使用bc提供的Provider
//		Provider provider = new BouncyCastleProvider();
//		BC = provider;
//		Security.addProvider(provider);
//	}
	
	/**
	 * 生成密钥
	 * 
	 * @param seed 种子
	 * @return 密钥对象
	 */
	public static Map<String, Key> initKey(String seed) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA, BC);
			// 初始化随机产生器
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.setSeed(seed.getBytes());
			keygen.initialize(1024, secureRandom);
			
			KeyPair keys = keygen.genKeyPair();
			
			PublicKey publicKey = keys.getPublic();
			BCUtils.publicKey = publicKey;
			PrivateKey privateKey = keys.getPrivate();
			BCUtils.privateKey = privateKey;
			
			Map<String, Key> map = new HashMap<String, Key>(2);
			map.put(PUBLIC_KEY, publicKey);
			map.put(PRIVATE_KEY, privateKey);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用公钥对数据进行RSA加密
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(byte[] data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);// x509支持公钥，pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA, BC);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行RSA加密
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(byte[] data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行RSA加密
	 * @param data 待加密的数据
	 * @param publicKey base64编码的公钥
	 * @return 加密后的数据，base64编码
	 */
	public static byte[] encrypt(byte[] data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return encrypt(data, pubKey);
	}
	
	/**
	 * 使用公钥对数据进行RSA加密，返回base64编码的字符
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据，base64编码
	 */
	public static String encrypts(byte[] data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA, BC);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行RSA加密，返回base64编码的字符
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据，base64编码
	 */
	public static String encrypts(byte[] data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行RSA加密，返回base64编码的字符
	 * @param data 待加密的数据
	 * @param publicKey base64编码的公钥
	 * @return 加密后的数据，base64编码
	 */
	public static String encrypts(byte[] data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return encrypts(data, pubKey);
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(byte[] data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA, BC);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, priKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(byte[] data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥，base64编码
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(byte[] data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return decrypt(data, priKey);
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(byte[] data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA, BC);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, priKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return new String(decrypt, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(byte[] data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA, BC);
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return new String(decrypt, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据
	 * @param privateKey 私钥，base64编码
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(byte[] data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return decrypts(data, priKey);
	}

	public static PublicKey getPublicKey() {
		return publicKey;
	}

	public static void setPublicKey(PublicKey publicKey) {
		BCUtils.publicKey = publicKey;
	}

	public static PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static void setPrivateKey(PrivateKey privateKey) {
		BCUtils.privateKey = privateKey;
	}
}
