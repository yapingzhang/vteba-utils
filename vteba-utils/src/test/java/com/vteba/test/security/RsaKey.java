package com.vteba.test.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RsaKey {

	public PublicKey getPublicKey(String modulus, String publicExponent)
			throws Exception {

		BigInteger m = new BigInteger(modulus);

		BigInteger e = new BigInteger(publicExponent);

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return publicKey;

	}

	public PrivateKey getPrivateKey(String modulus, String privateExponent)
			throws Exception {

		BigInteger m = new BigInteger(modulus);

		BigInteger e = new BigInteger(privateExponent);

		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;

	}

	public static void main(String[] args) throws Exception {

		String modulus = "10103166745709600780215616551837697832816413714471062522342538060943596036859967333870827790358555455232243383580565187280643159050869924436081447583051139";

		String publicExponent = "65537";

		String privateExponet = "367979294475011322800474185715497882523349856362702385535371444397399388741997039894583483410120364529325888461124714276674612930833020362278754665756193";

		RsaKey key = new RsaKey();

		PublicKey publicKey = key.getPublicKey(modulus, publicExponent);

		PrivateKey privateKey = key.getPrivateKey(modulus, privateExponet);

		// 加解密类

		Cipher cipher = Cipher.getInstance("RSA"); // "RSA/ECB/PKCS1Padding"
													// 就是：“算法/工作模式/填充模式”

		// 明文

		byte[] plainText = "hello world !".getBytes();

		// 加密

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] enBytes = cipher.doFinal(plainText);

		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] deBytes = cipher.doFinal(enBytes);

		String s = new String(deBytes);

		System.out.println(s);

	}

}
