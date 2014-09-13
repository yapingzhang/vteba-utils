package com.vteba.utils.serialize;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vteba.utils.charstr.ByteUtils;
import com.vteba.utils.charstr.Char;

/**
 * 基于Protostuff的序列化和反序列化工具。简化版，主要的改进在于，反序列化时，不需要不需要传递对象了。
 * @author yinlei 
 * @since 2013-12-12 17:32
 */
public class ProtoUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtoUtils.class);
	
    /**
     * 将对象序列化成字节数组
     * @param object 要被序列化的对象
     * @return 序列化后的字节数组
     */
    public static byte[] toBytes(Object object) {
        if (object == null) {
            return null;
        }
        byte[] bytes = Protos.toByteArray(object);
        int byteLength = bytes.length;
        String className = object.getClass().getName();
        byte[] nameBytes = className.getBytes(Char.UTF8);
        int length = nameBytes.length;
        byte[] lengthBytes = ByteUtils.toBytes(length);
        byte[] destBytes = new byte[byteLength + length + 4];
        
        System.arraycopy(lengthBytes, 0, destBytes, 0, 4);
        System.arraycopy(nameBytes, 0, destBytes, 4, length);
        System.arraycopy(bytes, 0, destBytes, length + 4, byteLength);
        
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
        
        int length = ByteUtils.toInt(lengthBytes);
        byte[] nameBytes = new byte[length];
        System.arraycopy(bytes, 4, nameBytes, 0, length);
        
        String className = new String(nameBytes, Char.UTF8);
        T entity = null;
        try {
			Class<?> clazz = Class.forName(className);
			@SuppressWarnings("unchecked")
			T temp = (T) clazz.newInstance();
			entity = temp;
		} catch (ClassNotFoundException e) {
			LOGGER.warn("没有找到类异常，class=[{}]", className, e.getMessage());
			return entity;
		} catch (InstantiationException e) {
			LOGGER.warn("类实例化异常，class=[{}]", className, e.getMessage());
			return entity;
		} catch (IllegalAccessException e) {
			LOGGER.warn("非法访问异常，class=[{}]", className, e.getMessage());
			return entity;
		}
        
        int destLength = byteLength - length - 4;
        byte[] destBytes = new byte[destLength];
        System.arraycopy(bytes, length + 4, destBytes, 0, destLength);
        
        Protos.mergeFrom(destBytes, entity);
        return entity;
    }
    
    public static void main(String[] aa) {
    	TestUser user = new TestUser();
    	user.setAge(250);
    	user.setUserName("haojiahuowo是牛年");
    	user.setDate(new Date());
    	byte[] bytes = toBytes(user);
    	System.out.println(bytes);
    	TestUser user2 = fromBytes(bytes);
    	System.out.println(user2);
    }
    
    
}
