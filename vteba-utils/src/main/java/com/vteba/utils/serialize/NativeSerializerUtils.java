package com.vteba.utils.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDK的序列化工具类。
 * @author yinlei
 * date 2012-9-18 下午9:20:37
 */
public class NativeSerializerUtils {
	private static final int BYTE_ARRAY_SIZE = 1024 * 1024;
	private static final Logger logger = LoggerFactory.getLogger(NativeSerializerUtils.class);
	
	/**
	 * 将对象序列化
	 * @param object 要被序列化的对象
	 * @return 对象被序列化后的字节数组
	 */
	public static byte[] fromObjectToBinary(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			try {
				oos.writeObject(object);
				return baos.toByteArray();
			} finally {
				IOUtils.closeQuietly(oos);
				IOUtils.closeQuietly(baos);
			}
		} catch (IOException ex) {
			logger.error("from Object To Binary serializer error.", ex);
		}
		return null;
	}
	
	/**
	 * 反序列对象化
	 * @param binary 要被反序列化的字节数组
	 * @return 序列化后的对象
	 */
	public static Object fromBinaryToObject(byte[] binary) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(binary);
			ObjectInputStream ois = new ObjectInputStream(bais);
			try {
				return ois.readObject();
			} finally {
				IOUtils.closeQuietly(ois);
				IOUtils.closeQuietly(bais);
			}
		} catch (Exception ex) {
			logger.error("from Binary To Object deserializer error.", ex);
		}
		return null;
	}
}
