package com.vteba.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.vteba.utils.charstr.ByteUtils;
import com.vteba.utils.common.IntCounter;
import com.vteba.utils.reflection.ReflectUtils;
import com.vteba.utils.serialize.Protos;

public class ProtoBufUtils {
	private static final ConcurrentMap<Integer, Class<?>> MAP = new ConcurrentHashMap<Integer, Class<?>>();
	private static final ConcurrentMap<Class<?>, Integer> INDEX = new ConcurrentHashMap<Class<?>, Integer>();
	
	/**
     * 将对象序列化成字节数组
     * @param object 要被序列化的对象
     * @return 序列化后的字节数组
     */
    public static byte[] toBytes(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        int index = 0;
        if (INDEX.get(clazz) == null) {
        	index = IntCounter.getAndIncrement();
        	MAP.put(index, clazz);
        	INDEX.put(clazz, index);
        } else {
        	index = INDEX.get(clazz);
        }
        
        byte[] bytes = Protos.toByteArray(object);
        int byteLength = bytes.length;
//        String className = object.getClass().getName();
//        byte[] nameBytes = className.getBytes(Char.UTF8);
//        int length = nameBytes.length;
        byte[] lengthBytes = ByteUtils.toBytes(index);//ByteUtils.toBytes(length);
        byte[] destBytes = new byte[byteLength + 4];
        
        System.arraycopy(lengthBytes, 0, destBytes, 0, 4);
//        System.arraycopy(nameBytes, 0, destBytes, 4, length);
        System.arraycopy(bytes, 0, destBytes, 4, byteLength);
        
        return destBytes;
    }
    
    /**
     * 将字节数组反序列化成对象
     * @param bytes 字节数组
     * @return 反序列化后的对象
     */
    public static <T> T fromBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int byteLength = bytes.length;
        
        byte[] lengthBytes = new byte[4];
        System.arraycopy(bytes, 0, lengthBytes, 0, 4);
        
        int index = ByteUtils.toInt(lengthBytes);
        Class<?> clazz = MAP.get(index);
        
//        byte[] nameBytes = new byte[length];
//        System.arraycopy(bytes, 4, nameBytes, 0, length);
//        
//        String className = new String(nameBytes, Char.UTF8);
        T entity = (T) ReflectUtils.instantiate(clazz);
        
        int destLength = byteLength - 4;
//        byte[] destBytes = new byte[destLength];
//        System.arraycopy(bytes, length + 4, destBytes, 0, destLength);
//        
//        Protos.mergeFrom(destBytes, entity);
        Protos.mergeFrom(bytes, 4, destLength, entity);
        return entity;
    }
}
