package com.vteba.utils.serialize;

import com.vteba.utils.charstr.ByteUtils;
import com.vteba.utils.charstr.Char;
import com.vteba.utils.common.ClassUtils;
import com.vteba.utils.reflection.AsmUtils;

/**
 * 基于Protostuff的序列化和反序列化工具。简化版，主要的改进在于，反序列化时，不需要传递对象了。
 * 性能稍差，主要的损失在于反射构造对象。
 * @author yinlei 
 * @since 2013-12-12 17:32
 */
public class ProtoUtils {
	
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
        Class<T> clazz = ClassUtils.forName(className);
        // 这个比直接反射性能高
        T entity = AsmUtils.get().invokeConstructor(clazz);//ReflectUtils.instantiate(className);
//        if (entity == null) {
//        	return null;
//        }
        
        int destLength = byteLength - length - 4;
//        byte[] destBytes = new byte[destLength];
//        System.arraycopy(bytes, length + 4, destBytes, 0, destLength);
//        
//        Protos.mergeFrom(destBytes, entity);
        Protos.mergeFrom(bytes, length + 4, destLength, entity);
        return entity;
    }

}
