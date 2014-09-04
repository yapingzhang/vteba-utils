package com.vteba.utils.reflection;

import java.util.HashMap;
import java.util.Map;

import com.vteba.lang.bytecode.ConstructorAccess;
import com.vteba.lang.bytecode.FieldAccess;
import com.vteba.lang.bytecode.MethodAccess;

/**
 * 字节码操作JavaBean，对象方法调用，属性设置、获取，构造对象实例
 * @author yinlei
 * date 2013-6-8 下午10:53:16
 */
public class AsmUtils {
	/** 缓存MethodAccess */
	private static Map<Class<?>, MethodAccess> methodAccessCache = new HashMap<Class<?>, MethodAccess>();
	/** 缓存FieldAccess */
	private static Map<Class<?>, FieldAccess> fieldAccessCache = new HashMap<Class<?>, FieldAccess>();
	/** 缓存ConstructorAccess */
	private static Map<Class<?>, ConstructorAccess<?>> constructorAccessCache = new HashMap<Class<?>, ConstructorAccess<?>>();
	
	private static AsmUtils instance = new AsmUtils();
	
	private AsmUtils() {
		
	}
	
    /**
     * 获得AsmUtils单例
     */
    public static AsmUtils get() {
        return instance;
    } 
    
    /**
     * 创建被访问类clazz的方法访问器
     * @param clazz 被访问对象的Class
     * @return com.skmbw.util.reflection.MethodAccess 方法访问器实例
     */
    public <T> MethodAccess createMethodAccess(Class<T> clazz) {
    	MethodAccess methodAccess = methodAccessCache.get(clazz);
        if (methodAccess == null){ 
            methodAccess = MethodAccess.get(clazz);
            methodAccessCache.put(clazz, methodAccess); 
        }
        return methodAccess;
    }
    
    /**
     * 创建被访问对象object的属性访问器
     * @param clazz 被访问对象的Class
     * @return com.skmbw.util.reflection.FieldAccess 属性访问器实例
     * @author yinlei
     * date 2013-6-6 下午9:45:34
     */
    public <T> FieldAccess createFieldAccess(Class<T> clazz) {
    	FieldAccess fieldAccess = fieldAccessCache.get(clazz);
    	if (fieldAccess == null) {
    		fieldAccess = FieldAccess.get(clazz);
    		fieldAccessCache.put(clazz, fieldAccess);
    	}
    	return fieldAccess;
    }
    
    /**
     * 创建被访问对象clazz的构造函数访问器
     * @param clazz 被访问对象的Class
     * @return com.skmbw.util.reflection.ConstructorAccess 构造函数访问器实例
     * @author yinlei
     * date 2013-6-6 下午9:53:51
     */
    public <T> ConstructorAccess<T> createConstructorAccess(Class<T> clazz) {
    	@SuppressWarnings("unchecked")
    	ConstructorAccess<T> access = (ConstructorAccess<T>) constructorAccessCache.get(clazz);
    	if (access == null) {
    		access = ConstructorAccess.get(clazz);
    		constructorAccessCache.put(clazz, access);
    	}
    	return access;
    }
    
    /**
     * 调用目标对象的指定方法
     * @param target 目标对象
     * @param methodName 方法名
     * @param args 方法参数
     * @return 方法调用结果
     * @author yinlei
     * date 2013-6-6 下午11:35:37
     */
    public Object invokeMethod(Object target, String methodName, Object... args) {
    	return createMethodAccess(target.getClass()).invoke(target, methodName, args);
    }
    
    /**
     * 获取目标对象的属性值
     * @param target 目标对象
     * @param fieldName 属性名
     * @return 属性值
     * @author yinlei
     * date 2013-6-6 下午11:37:31
     */
    public Object getField(Object target, String fieldName) {
    	return createFieldAccess(target.getClass()).get(target, fieldName);
    }
    
    /**
     * 设置目标对象的属性值
     * @param target 目标对象
     * @param fieldName 属性名
     * @param value 属性值
     * @author yinlei
     * date 2013-6-6 下午11:39:03
     */
    public void setField(Object target, String fieldName, Object value) {
    	createFieldAccess(target.getClass()).set(target, fieldName, value);
    }
    
    /**
     * 调用目标类targetClazz的默认构造函数，构造实例
     * @param targetClazz 被调用的目标类
     * @return targetClazz实例
     * @author yinlei
     * date 2013-6-6 下午11:30:05
     */
    public <T> T invokeConstructor(Class<T> targetClazz) {
    	return createConstructorAccess(targetClazz).newInstance();
    }
    
    /**
     * 调用目标类targetClazz的指定构造函数，构造实例
     * @param targetClazz 被调用的目标类
     * @param args 构造函数参数
     * @return targetClazz实例
     * @author yinlei
     * date 2013-6-6 下午11:25:01
     */
    public <T> T invokeConstructor(Class<T> targetClazz, Object args) {
    	return createConstructorAccess(targetClazz).newInstance(args);
    }
}
