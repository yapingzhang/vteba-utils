package com.vteba.test;

import org.junit.Test;

import com.vteba.utils.charstr.Char;

public class NativeNewTest {
	@Test
	public void test() {
		String str = "\tyinlei尹雷\n";
		byte[] bytes = str.getBytes(Char.UTF8);
		
		long d = System.nanoTime();
		String string = new String(bytes, 1, bytes.length - 2, Char.UTF8);
		string.getBytes(Char.UTF8);
		System.out.println(System.nanoTime() - d);
		
		d = System.nanoTime();
		byte[] dest = new byte[bytes.length - 2];
		System.arraycopy(bytes, 1, dest, 0, dest.length);
		System.out.println(System.nanoTime() - d);
	}
}
