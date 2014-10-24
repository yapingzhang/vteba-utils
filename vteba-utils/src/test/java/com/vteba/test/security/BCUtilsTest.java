package com.vteba.test.security;

import org.junit.Test;

import com.vteba.utils.cryption.BCUtils;

public class BCUtilsTest {
	@Test
	public void test() {
		BCUtils.initKey("hjasdfjkahsdfhjklasdfhkas2121hasdf");
		String data = "haogafsdaf";
		byte[] encrypt = BCUtils.encrypt(data.getBytes(), BCUtils.getPublicKey());
		String result = BCUtils.decrypts(encrypt, BCUtils.getPrivateKey());
		System.out.println("结果：" + result);
	}
}
