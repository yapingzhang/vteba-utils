package com.vteba.test.security;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.vteba.utils.cryption.RSAUtils;

public class RSAUtilsTest {
	@Test
	public void test() throws Exception {
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		// 构建密钥
		Map<String, Key> keyMap = RSAUtils.initKey();

		// 获得密钥
		String publicKey = RSAUtils.getPublicKey(keyMap);
		String privateKey = RSAUtils.getPrivateKey(keyMap);

		System.err.println("公钥:\r" + publicKey);
		System.err.println("私钥:\r" + privateKey);

		// 产生签名
		String sign = RSAUtils.signs(data, privateKey);
		System.err.println("签名:\r" + sign);

		// 验证签名
		boolean status = RSAUtils.verify(data, publicKey, sign);
		System.err.println("状态:\r" + status);
		Assert.assertTrue(status);

	}
	
	@Test
	public void test3() throws Exception {
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		// 构建密钥
		Map<String, Key> map = RSAUtils.initKey();

		// 产生签名
		String sign = RSAUtils.signs(data, (PrivateKey)map.get("RSAPrivateKey"));
		System.err.println("签名2:\r" + sign);

		// 验证签名
		boolean status = RSAUtils.verify(data, (PublicKey)map.get("RSAPublicKey"), sign);
		System.err.println("状态2:\r" + status);
		Assert.assertTrue(status);
		
		// 加密
		PublicKey publicKey = (PublicKey) map.get(RSAUtils.PUBLIC_KEY);
		byte[] encrypt = RSAUtils.encrypt(data, publicKey);
		System.err.println("加密:" + new String(encrypt));
		
		String encrypts = RSAUtils.encrypts(data, publicKey);
		System.err.println("加密s:" + encrypts);

		byte[] encryptByte = RSAUtils.encrypt(data, publicKey.getEncoded());
		
		// 解密
		PrivateKey privateKey = (PrivateKey) map.get(RSAUtils.PRIVATE_KEY);
		byte[] decrypt = RSAUtils.decrypt(encrypt, privateKey);
		System.err.println("解密:" + new String(decrypt));
		Assert.assertEquals(inputStr, new String(decrypt));

		String decrypts = RSAUtils.decrypts(encrypts, privateKey);
		System.out.println("解密s：" + decrypts);
		
		String result = RSAUtils.decrypts(encryptByte, privateKey.getEncoded());
		System.out.println("解密byte：" + result);
	}
	
	@Test
	public void test4() {
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		// 构建密钥
		Map<String, Key> keyMap = RSAUtils.initKey();
		PrivateKey privateKey = (PrivateKey) keyMap.get("RSAPrivateKey");
		byte[] encoded = RSAUtils.encode(data, privateKey.getEncoded());
		PublicKey publicKey = (PublicKey) keyMap.get("RSAPublicKey");
		byte[] result = RSAUtils.decode(encoded, publicKey.getEncoded());
		System.out.println("私钥加密公钥解密：" + new String(result));
	}
}
