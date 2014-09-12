package com.vteba.utils.serialize;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.vteba.utils.charstr.Char;

/**
 * 基于Protostuff的序列化和反序列化工具。简化版，主要的改进在于，反序列化时，不需要不需要传递对象了。
 * @author yinlei 
 * @since 2013-12-12 17:32
 */
public class ProtoUtils {
    private static Map<Integer, Class<?>> cache = new HashMap<Integer, Class<?>>();
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
        byte[] headers = new byte[8];
        byte[] destBytes = new byte[bytes.length + 8];
        object.getClass().getName().getBytes(Char.UTF8);
        return bytes;
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
        
        return null;
    }
}
