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

/**
 * 数字签名算法工具。DSA (Digital Signature Algorithm)
 * DSA 一般用于数字签名和认证。
 * DSA是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(Digital Signature Standard)。
 * DSA是基于整数有限域离散对数难题的，其安全性与RSA相比差不多。
 * 在DSA数字签名和认证中，发送者使用自己的私钥对文件或消息进行签名，接受者收到消息后使用发送者的公钥
 * 来验证签名的真实性。DSA和RSA不同之处在于它不能用作加密和解密，也不能进行密钥交换，
 * 只用于签名，它比RSA要快很多。
 * @author yinlei
 * @date 2012-12
 */
public class DSAUtils {
	private static final String ALGORITHM = "DSA";

	/**默认种子*/
	private static final String DEFAULT_SEED = "0f22507a10b7872323$####@@bddd07asd45542@##@#@d8a308212;;;2966e3";

	private static final String PUBLIC_KEY = "DSAPublicKey";
	private static final String PRIVATE_KEY = "DSAPrivateKey";
	
	/**
	 * 用私钥对信息，生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥，base64编码的
	 * @return 数字签名，base64编码的
	 */
	public static String signs(byte[] data, String privateKey) {
		// 解密由base64编码的私钥
		byte[] keyBytes = CryptUtils.base64Decode(privateKey);
		return signs(data, keyBytes);
	}
	
	/**
	 * 用私钥对信息，生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥
	 * @return 数字签名，base64编码的
	 */
	public static String signs(byte[] data, byte[] privateKey) {
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);//支持私钥，而X509EncodedKeySpec支持公钥
		try {
			// 获得加密算法key工厂
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			// 获取私钥匙对象，从提供的私钥字符串中
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			return signs(data, priKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥
	 * @return 数字签名，base64编码的字符串
	 */
	public static String signs(byte[] data, PrivateKey privateKey) {
		try {
			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return CryptUtils.base64Encode(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 用私钥对信息，生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥，base64编码的
	 * @return 数字签名，字节数组
	 */
	public static byte[] sign(byte[] data, String privateKey) {
		// 解密由base64编码的私钥
		byte[] keyBytes = CryptUtils.base64Decode(privateKey);
		return sign(data, keyBytes);
	}
	
	/**
	 * 用私钥对信息，生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥
	 * @return 数字签名，字节数组
	 */
	public static byte[] sign(byte[] data, byte[] privateKey) {
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);//支持私钥，而X509EncodedKeySpec支持公钥
		try {
			// 获得加密算法key工厂
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			// 获取私钥匙对象，从提供的私钥字符串中
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

			return sign(data, priKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data 要签名的数据
	 * @param privateKey 私钥
	 * @return 数字签名，字节数组
	 */
	public static byte[] sign(byte[] data, PrivateKey privateKey) {
		try {
			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否被篡改
	 * 
	 * @param data 被签名地数据（明文，原始数据）
	 * @param publicKey 公钥，base64编码的字符串
	 * @param sign 数字签名，base64编码的字符串
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		// 解密由base64编码的公钥
		byte[] keyBytes = CryptUtils.base64Decode(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);// 这种方式可以将密钥以字符串的形式放在文件中，//支持公钥，而PKCS8EncodedKeySpec支持私钥
		try {
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return verify(data, pubKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否被篡改
	 * 
	 * @param data 被签名地数据（明文，原始数据）
	 * @param publicKey 公钥
	 * @param sign 数字签名，base64编码的字符串
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, byte[] publicKey, String sign) {
		byte[] signBytes = CryptUtils.base64Decode(sign);
		return verify(data, publicKey, signBytes);
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否是被篡改
	 * 
	 * @param data 被签名的数据（明文，原始数据）
	 * @param publicKey DSA公钥
	 * @param sign 数字签名，base64编码的字符串
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, PublicKey publicKey, String sign) {
		byte[] signBytes = CryptUtils.base64Decode(sign);
		return verify(data, publicKey, signBytes);
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否被篡改
	 * 
	 * @param data 被签名地数据（明文，原始数据）
	 * @param publicKey 公钥，base64编码的字符串
	 * @param sign 数字签名，base64编码的字符串
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, String publicKey, byte[] sign) {
		// 解密由base64编码的公钥
		byte[] keyBytes = CryptUtils.base64Decode(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);//支持公钥，而PKCS8EncodedKeySpec支持私钥
		try {
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return verify(data, pubKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否是被篡改
	 * 
	 * @param data 被签名的数据（明文，原始数据）
	 * @param publicKey DSA公钥
	 * @param sign 数字签名
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign) {
		try {
			Signature signature = Signature.getInstance(ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data);
			// 验证签名是否正常
			return signature.verify(sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据公钥和数字签名校验加密数据是否被篡改
	 * 
	 * @param data 被签名地数据（明文，原始数据）
	 * @param publicKey 公钥
	 * @param sign 数字签名
	 * @return 校验成功返回true 失败返回false
	 */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) {
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);//支持公钥，而PKCS8EncodedKeySpec支持私钥
		try {
			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return verify(data, pubKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 生成密钥，默认1024位。
	 * DSAPublicKey是公钥map key，DSAPrivateKey是私钥map key。
	 * @param seed 种子
	 * @return 密钥对象
	 */
	public static Map<String, Key> initKey(String seed) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
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
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 默认生成密钥
	 * DSAPublicKey是公钥map key，DSAPrivateKey是私钥map key。
	 * @return 密钥对象
	 */
	public static Map<String, Key> initKey() {
		return initKey(DEFAULT_SEED);
	}

	/**
	 * 取得私钥
	 * 
	 * @param keyMap 保存密钥的map
	 * @return 私钥字符串，base64编码
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) {
		Key key = keyMap.get(PRIVATE_KEY);
		return CryptUtils.base64Encode(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap 保存密钥的map
	 * @return 公钥字符串，base64编码
	 */
	public static String getPublicKey(Map<String, Key> keyMap) {
		Key key = keyMap.get(PUBLIC_KEY);
		return CryptUtils.base64Encode(key.getEncoded());
	}
}
