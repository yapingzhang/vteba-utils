package com.vteba.utils.cryption;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 公钥加密算法，非对称加密
 * 
 * @author yinlei
 * @date 2012-12-1
 */
public class RSAUtils {
	/** 可以使用DSA方式获得签名，也可以使用RSA方式获得签名，注意匹配，成对出现。 */
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA512withRSA";

	/**
	 * 默认种子
	 */
	private static final String DEFAULT_SEED = "0f22507a10bbddd07asd45542@##@#@d8a3082122966e3";

	public static final String PUBLIC_KEY = "DSAPublicKey";
	public static final String PRIVATE_KEY = "DSAPrivateKey";
	
	private static final Map<String, Key> keyMap = new HashMap<String, Key>();

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据，要签名的数据
	 * @param privateKey
	 *            私钥
	 * @return 数字签名
	 */
	public static String sign(byte[] data, String privateKey) {
		// 解密由base64编码的私钥
		byte[] keyBytes = CryptUtils.base64Decode(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		try {
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 取私钥匙对象
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(priKey);
			signature.update(data);
			return CryptUtils.base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据，要签名的数据
	 * @param privateKey
	 *            私钥
	 * @return 数字签名
	 */
	public static String sign(byte[] data, PrivateKey privateKey) {
		try {
			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return CryptUtils.base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据，要签名的数据
	 * @param privateKey
	 *            私钥
	 * @return 数字签名
	 */
	public static String sign(byte[] data) {
		try {
			// 取私钥匙对象
			PrivateKey priKey = (PrivateKey) keyMap.get(PRIVATE_KEY);

			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(priKey);
			signature.update(data);
			return CryptUtils.base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		// 解密由base64编码的公钥
		byte[] keyBytes = CryptUtils.base64Decode(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);// 这种方式可以将密钥以字符串的形式放在文件中

		try {
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(pubKey);
			signature.update(data);
			
			// 验证签名是否正常
			return signature.verify(CryptUtils.base64Decode(sign));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * 
	 */
	public static boolean verify(byte[] data, PublicKey publicKey, String sign) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data);
			
			// 验证签名是否正常
			return signature.verify(CryptUtils.base64Decode(sign));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * 
	 */
	public static boolean verify(byte[] data, String sign) {
		try {
			// 取公钥匙对象
			PublicKey pubKey = (PublicKey) keyMap.get(PUBLIC_KEY);
			
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(pubKey);
			signature.update(data);
			
			// 验证签名是否正常
			return signature.verify(CryptUtils.base64Decode(sign));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 生成密钥
	 * 
	 * @param seed
	 *            种子
	 * @return 密钥对象
	 */
	public static Map<String, Key> initKey(String seed) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			// 初始化随机产生器
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.setSeed(seed.getBytes());
			keygen.initialize(1024, secureRandom);
			
			KeyPair keys = keygen.genKeyPair();
			
			PublicKey publicKey = keys.getPublic();
			PrivateKey privateKey = keys.getPrivate();
			
			Map<String, Key> map = new HashMap<String, Key>(2);
			map.put(PUBLIC_KEY, publicKey);
			map.put(PRIVATE_KEY, privateKey);
			keyMap.put(PUBLIC_KEY, publicKey);
			keyMap.put(PRIVATE_KEY, privateKey);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认生成密钥
	 * 
	 * @return 密钥对象
	 */
	public static Map<String, Key> initKey() {
		return initKey(DEFAULT_SEED);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap 保存密钥的map
	 * @return 私钥字符串
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) {
		Key key = keyMap.get(PRIVATE_KEY);
		return CryptUtils.base64Encode(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap 保存密钥的map
	 * @return 公钥字符串
	 */
	public static String getPublicKey(Map<String, Key> keyMap) {
		Key key = keyMap.get(PUBLIC_KEY);
		return CryptUtils.base64Encode(key.getEncoded());
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
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行RSA加密，返回base64编码的字符
	 * @param data 待加密的数据
	 * @param publicKey 公钥
	 * @return 加密后的数据
	 */
	public static String encrypts(byte[] data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(encrypt);
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
			Cipher cipher = Cipher.getInstance("RSA");
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
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static String decrypts(byte[] data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return new String(decrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码的字符串
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] encrypt = CryptUtils.base64Decode(data);
			byte[] decrypt = cipher.doFinal(encrypt);
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64加密的字符串
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static String decrypts(String data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] encrypt = CryptUtils.base64Decode(data);
			byte[] decrypt = cipher.doFinal(encrypt);
			return new String(decrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
