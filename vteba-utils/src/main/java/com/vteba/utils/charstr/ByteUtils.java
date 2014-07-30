package com.vteba.utils.charstr;

import com.vteba.utils.serialize.Kryos;

public class ByteUtils {
	public static byte[][] toBytes(String... keys) {
		byte[][] bytes = new byte[keys.length][0];
		int i = 0;
		for (String value : keys) {
			bytes[i++] = value.getBytes();
		}
		return bytes;
	}
	
	public static byte[][] toBytes(Object... objects) {
		byte[][] bytes = new byte[objects.length][0];
		int i = 0;
		for (Object value : objects) {
			bytes[i++] = Kryos.toBytes(value);
		}
		return bytes;
	}
	
	public static void main(String[] args) {
		toBytes("a尹雷", "b");
	}
}
