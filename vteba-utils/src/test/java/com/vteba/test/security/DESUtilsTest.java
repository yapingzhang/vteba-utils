package com.vteba.test.security;

import org.junit.Test;

import com.vteba.utils.cryption.DESUtils;

public class DESUtilsTest {
	@Test
	public static void test() {
		String str = "root";
		String pass = "3416763";
		String enstr = DESUtils.getEncrypt(str);
		String enpass = DESUtils.getEncrypt(pass);
		System.out.println(enstr);
		System.out.println(enpass);
		System.out.println(DESUtils.getDecrypt(enstr));
		System.out.println(DESUtils.getDecrypt(enpass));
		
		enstr = DESUtils.aesEncrypt(str.getBytes());
		enpass = DESUtils.aesEncrypt(pass.getBytes());
		System.out.println(enstr);
		System.out.println(enpass);
		System.out.println(DESUtils.aesDecrypt(enstr));
		System.out.println(DESUtils.aesDecrypt(enpass));
		
		enstr = DESUtils.desedeEncrypt(str.getBytes());
		enpass = DESUtils.desedeEncrypt(pass.getBytes());
		System.out.println(enstr);
		System.out.println(enpass);
		System.out.println(DESUtils.desedeDecrypt(enstr));
		System.out.println(DESUtils.desedeDecrypt(enpass));
	}
}
