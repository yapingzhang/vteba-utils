package com.vteba.test.security;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.vteba.utils.cryption.DSAUtils;

public class DSAUtilsTest {

	@Test
	public void test() {
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();

		// 构建密钥
		Map<String, Key> map = DSAUtils.initKey();

		// 产生签名
		String sign = DSAUtils.signs(data, (PrivateKey)map.get("DSAPrivateKey"));
		System.err.println("签名2:\r" + sign);

		// 验证签名
		boolean status = DSAUtils.verify(data, (PublicKey)map.get("DSAPublicKey"), sign);
		System.err.println("状态2:\r" + status);
		Assert.assertTrue(status);
		
		
	}
}
