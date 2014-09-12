package com.vteba.utils.serialize;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.marshalling.ByteBufferInput;
import org.jboss.marshalling.ByteBufferOutput;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于JBoss Marshalling的序列化和反序列化工具。
 * @author yinlei 
 * @since 2013-12-12 17:02
 */
public class MarshaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarshaUtils.class);
    
    public static final int SIZE = 2048;
    protected static Marshaller marshaller = null;
    protected static Unmarshaller unmarshaller = null;
    static {
        MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        try {
            marshaller = factory.createMarshaller(configuration);
            unmarshaller = factory.createUnmarshaller(configuration);
        } catch (IOException e) {
            LOGGER.error("初始化JBoss编解码器出错。", e.getMessage());
        }
    }
    
    /**
     * 将对象序列化成字节数组
     * @param object 要被序列化的对象
     * @return 序列化后的字节数组
     */
    public static byte[] toBytes(Object object) {
        if (object == null) {
            return null;
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        ByteOutput byteOutput = new ByteBufferOutput(byteBuffer);
        try {
            marshaller.start(byteOutput);
            marshaller.writeObject(object);
            marshaller.finish();
            marshaller.flush();
        } catch (IOException e) {
            LOGGER.error("JBoss编解码器，序列化对象IO错误。", e.getMessage());
        }
        return byteBuffer.array();
    }
    
    /**
     * 将对象序列化成字节buffer
     * @param object 要被序列化的对象
     * @return 序列化后的字节buffer
     */
    public static ByteBuffer toByteBuffer(Object object) {
        if (object == null) {
            return null;
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
        ByteOutput byteOutput = new ByteBufferOutput(byteBuffer);
        try {
            marshaller.start(byteOutput);
            marshaller.writeObject(object);
            marshaller.finish();
            marshaller.flush();
        } catch (IOException e) {
            LOGGER.error("JBoss编解码器，序列化对象IO错误。", e.getMessage());
        }
        return byteBuffer;
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
        
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(SIZE);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            ByteInput newInput = new ByteBufferInput(byteBuffer);
            unmarshaller.start(newInput);
            @SuppressWarnings("unchecked")
            T t = (T) unmarshaller.readObject();
            unmarshaller.finish();
            return t;
        } catch (IOException e) {
            LOGGER.error("JBoss编解码器，反序列化，对象IO错误。", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("JBoss编解码器，反序列化，没有找到对象类。", e.getMessage());
        }
        return null;
    }
    
    /**
     * 将字节buffer反序列化成对象
     * @param bytes 字节buffer
     * @return 反序列化后的对象
     */
    public static <T> T fromBytes(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return null;
        }
        
        try {
            byteBuffer.flip();
            ByteInput newInput = new ByteBufferInput(byteBuffer);
            unmarshaller.start(newInput);
            @SuppressWarnings("unchecked")
            T t = (T) unmarshaller.readObject();
            unmarshaller.finish();
            return t;
        } catch (IOException e) {
            LOGGER.error("JBoss编解码器，反序列化，对象IO错误。", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("JBoss编解码器，反序列化，没有找到对象类。", e.getMessage());
        }
        return null;
    }
}
