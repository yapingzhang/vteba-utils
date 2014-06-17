package com.vteba.utils.json;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * JSON工具类，使用fastjson实现。
 * @author yinlei
 * date 2013-8-17 下午12:28:25
 */
public class FastJsonUtils{
	private static Logger logger = LoggerFactory.getLogger(FastJsonUtils.class);
    
    private FastJsonUtils(){
    	
    }
    
    /**
     * 将对象序列化成json字符串。
     * @param obj 要被序列化的对象
     * @return json字符串
     * @author yinlei
     * @date 2013年10月13日 下午1:55:21
     */
    public static String toJson(Object obj) {
    	String json = JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    	if (logger.isInfoEnabled()) {
    		logger.info(json);
    	}
        return json;
    }
    
    /**
     * 将对象序列化成字节数组
     * @param obj 要被序列化的对象
     * @return 字节数组
     * @author yinlei
     * @date 2013年10月13日 下午1:56:41
     */
    public static byte[] toJsonBytes(Object obj) {
    	byte[] json = JSON.toJSONBytes(obj, SerializerFeature.WriteDateUseDateFormat);
        return json;
    }
    
    /**
     * 将json反序列化成java对象。Class&lt;T&gt;只能是JavaBean对象及其数组对象，不能是集合，Map。<br>
     * 当然，JavaBean内部是可以包含集合，Map的。
     * @param json json字符串
     * @param clazz 目标类型
     * @return Class&lt;T&gt;的实例
     * @author yinlei
     * @date 2013年10月13日 下午1:57:01
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
    	if (logger.isInfoEnabled()) {
    		logger.info(json);
    	}
    	return JSON.parseObject(json, clazz);
    }
    
    /**
     * 将json反序列化成java对象List。可使用{@link#fromJson(String, TypeReference)}代替，后者更通用。
     * @param json json字符串
     * @param clazz 目标类型
     * @return Class&lt;T&gt;的实例List
     * @author yinlei
     * @date 2013年10月13日 下午2:03:32
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
    	if (logger.isInfoEnabled()) {
    		logger.info(json);
    	}
    	return JSON.parseArray(json, clazz);
    }
    
    /**
     * 将json反序列化成java对象。适用于任何Java对象。TypeReference用法如：<br>
     * new TypeReference&lt;Map&lt;String, List&lt;User&gt;&gt;&gt;(){}，new TypeReference&lt;List&lt;User&gt;&gt;(){}，<br>
     * new TypeReference&lt;User&gt;(){}，new TypeReference&lt;User[]&gt;(){}<br>
     * 泛型参数Map&lt;String, List&lt;User&gt;&gt;，List&lt;User&gt;，User，User[]即为你要反序列化的目标类型。
     * @param json json字符串
     * @param reference 泛型类型引用
     * @return  &lt;T&gt;的实例
     * @author yinlei
     * @date 2013年10月13日 下午1:35:14
     */
    public static <T> T fromJson(String json, TypeReference<T> reference) {
    	if (logger.isInfoEnabled()) {
    		logger.info(json);
    	}
    	return JSON.parseObject(json, reference.getType());
    }
    
}