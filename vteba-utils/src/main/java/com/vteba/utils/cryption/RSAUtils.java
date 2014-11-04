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

import com.vteba.utils.charstr.Char;

/**
 * 公钥加密算法，非对称加密。可用于数据加密和解密，也可用于数据签名。安全性和DSA大体相当。
 * RSA的安全性是基于极其困难的大整数的分解（两个素数的乘积）。
 * @author yinlei
 * @date 2012-12-1
 */
public class RSAUtils {
	/** key算法使用RSA方式 */
	public static final String RSA = "RSA";
	/** 签名算法使用SHA512withRSA方式 */
	public static final String SIGN_ALGORITHM = "SHA512withRSA";

	/**默认种子*/
	private static final String DEFAULT_SEED = "0f22507a10bbddd07asd45542@##@#@d8a3082122966e3";

	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
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
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
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
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
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
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
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
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			// 取公钥匙对象
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return verify(data, pubKey, sign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化密钥，包括公钥和私钥。使用默认的安全随机数算法SHA1PRNG，key1024长度。
	 * @param seed 安全随机数种子
	 * @return 密钥对象Map
	 */
	public static Map<String, Key> initKey(String seed) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA);
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
	 * 初始化密钥，包括公钥和私钥。key长度1024
	 * @param secureRandom 安全随机数
	 * @return 密钥对象Map
	 */
	public static Map<String, Key> initKey(SecureRandom secureRandom) {
		return initKey(secureRandom, 1024);
	}
	
	/**
	 * 初始化密钥，包括公钥和私钥。
	 * @param secureRandom 安全随机数
	 * @param keysize key长度，建议1024
	 * @return 密钥对象Map
	 */
	public static Map<String, Key> initKey(SecureRandom secureRandom, int keysize) {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA);
			// 初始化随机产生器
			keygen.initialize(keysize, secureRandom);
			
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
	 * 默认生成密钥。key长度1024。使用默认盐值的安全随机数算法SHA1PRNG。
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
	@Deprecated
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
	@Deprecated
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
	public static byte[] encrypt(byte[] data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);// x509支持公钥，pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
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
			Cipher cipher = Cipher.getInstance(RSA);
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
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, priKey, secureRandom);
			byte[] decrypt = cipher.doFinal(CryptUtils.base64Decode(data));
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
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
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥，base64编码
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return decrypt(data, priKey);
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(String data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);// pkcs8支持私钥
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, priKey, secureRandom);
			byte[] decrypt = cipher.doFinal(CryptUtils.base64Decode(data));
			return new String(decrypt, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(String data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey, secureRandom);
			byte[] encrypt = CryptUtils.base64Decode(data);
			byte[] decrypt = cipher.doFinal(encrypt);
			return new String(decrypt, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行RSA解密
	 * @param data 待解密的数据，base64编码
	 * @param privateKey 私钥，base64编码
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decrypts(String data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return decrypts(data, priKey);
	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥
	 * @return 加密后的数据
	 */
	public static byte[] encode(byte[] data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			//算法名/工作模式/填充模式
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, priKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥
	 * @return 加密后的数据
	 */
	public static byte[] encode(byte[] data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return encrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥，base64编码的字符串
	 * @return 加密后的数据
	 */
	public static byte[] encode(byte[] data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return encode(data, priKey);
	}
	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥
//	 * @return 加密后的数据
//	 */
//	public static byte[] encode(String data, byte[] privateKey) {
//		try {
//			SecureRandom secureRandom = new SecureRandom();
//			Cipher cipher = Cipher.getInstance(RSA);
//			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
//			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
//			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
//			cipher.init(Cipher.ENCRYPT_MODE, priKey, secureRandom);
//			byte[] encrypt = cipher.doFinal(data.getBytes());
//			return encrypt;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥
//	 * @return 加密后的数据
//	 */
//	public static byte[] encode(String data, PrivateKey privateKey) {
//		try {
//			SecureRandom secureRandom = new SecureRandom();
//			Cipher cipher = Cipher.getInstance(RSA);
//			cipher.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom);
//			byte[] encrypt = cipher.doFinal(data.getBytes());
//			return encrypt;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥，base64编码的字符串
//	 * @return 加密后的数据
//	 */
//	public static byte[] encode(String data, String privateKey) {
//		byte[] priKey = CryptUtils.base64Decode(privateKey);
//		return encode(data, priKey);
//	}
//	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥
//	 * @return 加密后的数据，base64编码
//	 */
//	public static String encodes(String data, byte[] privateKey) {
//		try {
//			SecureRandom secureRandom = new SecureRandom();
//			Cipher cipher = Cipher.getInstance(RSA);
//			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
//			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
//			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
//			cipher.init(Cipher.ENCRYPT_MODE, priKey, secureRandom);
//			byte[] encrypt = cipher.doFinal(data.getBytes());
//			return CryptUtils.base64Encode(encrypt);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥
//	 * @return 加密后的数据，base64编码
//	 */
//	public static String encodes(String data, PrivateKey privateKey) {
//		try {
//			SecureRandom secureRandom = new SecureRandom();
//			Cipher cipher = Cipher.getInstance(RSA);
//			cipher.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom);
//			byte[] encrypt = cipher.doFinal(data.getBytes());
//			return CryptUtils.base64Encode(encrypt);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	/**
//	 * 使用私钥对数据进行rsa加密
//	 * @param data 待加密的数据
//	 * @param privateKey 私钥，base64编码的
//	 * @return 加密后的数据，base64编码
//	 */
//	public static String encodes(String data, String privateKey) {
//		byte[] priKey = CryptUtils.base64Decode(privateKey);
//		return encodes(data, priKey);
//	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥
	 * @return 加密后的数据，base64编码
	 */
	public static String encodes(byte[] data, byte[] privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, priKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥
	 * @return 加密后的数据，base64编码
	 */
	public static String encodes(byte[] data, PrivateKey privateKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom);
			byte[] encrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(encrypt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用私钥对数据进行rsa加密
	 * @param data 待加密的数据
	 * @param privateKey 私钥，base64编码的
	 * @return 加密后的数据，base64编码
	 */
	public static String encodes(byte[] data, String privateKey) {
		byte[] priKey = CryptUtils.base64Decode(privateKey);
		return encodes(data, priKey);
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥
	 * @return 解密后的数据
	 */
	public static byte[] decode(byte[] data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, pubKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥
	 * @return 解密后的数据
	 */
	public static byte[] decode(byte[] data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥，base64编码的
	 * @return 解密后的数据
	 */
	public static byte[] decode(byte[] data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return decode(data, pubKey);
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥
	 * @return 解密后的数据，base64编码
	 */
	public static String decodes(byte[] data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, pubKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(decrypt);// base64还是UTF-8，以后再确定吧
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥
	 * @return 解密后的数据，base64编码
	 */
	public static String decodes(byte[] data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey, secureRandom);
			byte[] decrypt = cipher.doFinal(data);
			return CryptUtils.base64Encode(decrypt);// base64还是UTF-8，以后再确定吧
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据
	 * @param publicKey 公钥，base64编码的
	 * @return 解密后的数据，base64编码
	 */
	public static String decodes(byte[] data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return decodes(data, pubKey);// base64还是UTF-8，以后再确定吧
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥
	 * @return 解密后的数据
	 */
	public static byte[] decode(String data, byte[] publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, pubKey, secureRandom);
			byte[] decrypt = cipher.doFinal(CryptUtils.base64Decode(data));
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥
	 * @return 解密后的数据
	 */
	public static byte[] decode(String data, PublicKey publicKey) {
		try {
			SecureRandom secureRandom = new SecureRandom();
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey, secureRandom);
			byte[] decrypt = cipher.doFinal(CryptUtils.base64Decode(data));
			return decrypt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥，base64编码的
	 * @return 解密后的数据
	 */
	public static byte[] decode(String data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return decode(data, pubKey);
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decodes(String data, byte[] publicKey) {
		return new String(decode(data, publicKey), Char.UTF8);
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decodes(String data, PublicKey publicKey) {
		return new String(decode(data, publicKey), Char.UTF8);
	}
	
	/**
	 * 使用公钥对数据进行rsa解密
	 * @param data 待解密的数据，base64编码的
	 * @param publicKey 公钥，base64编码的
	 * @return 解密后的数据，UTF-8编码
	 */
	public static String decodes(String data, String publicKey) {
		byte[] pubKey = CryptUtils.base64Decode(publicKey);
		return decodes(data, pubKey);
	}
}
