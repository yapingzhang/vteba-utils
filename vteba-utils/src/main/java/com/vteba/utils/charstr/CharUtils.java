package com.vteba.utils.charstr;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class CharUtils {
//	public static final Charset GBK = Charset.forName("GBK");
//	public static final Charset UTF8 = Charset.forName("UTF-8");
	
	public static byte[] gbkBytes(char c) {
		CharBuffer charBuffer = CharBuffer.allocate(1);
		charBuffer.put(c);
		charBuffer.flip();
		ByteBuffer byteBuffer = Char.GBK.encode(charBuffer);
		return byteBuffer.array();
	}
	
	public static byte[] gbkBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Char.GBK.encode(charBuffer);
		return byteBuffer.array();
	}
	
	public static byte[] gbkBytes(String value) {
		return value.getBytes(Char.GBK);
	}
	
	public static byte[] utf8Bytes(String value) {
		return value.getBytes(Char.UTF8);
	}
	
	public static void main(String[] args) {
		CharBuffer charBuffer = CharBuffer.allocate(3);
		charBuffer.put('c');
		charBuffer.put('2');
		charBuffer.put('a');
		System.out.println(Arrays.toString(gbkBytes('尹')));
		System.out.println(Arrays.toString(gbkBytes(new char[]{'尹','雷'})));
	}
	
}
