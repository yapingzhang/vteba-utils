package com.vteba.utils.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.vteba.utils.common.PropertiesLoader;

/**
 * 在不使用spring的情况下，属性文件加载。应用中可以仿此类，写一个PropsUtils
 * 工具类，需要更改的是类中PropertiesLoader的构造函数参数。
 * @author 尹雷 
 * @since 2014-6
 */
public class PropsUtils {
    protected static final Properties properties;
    
    static {
        PropertiesLoader loader = new PropertiesLoader("config.properties");
        properties = loader.getProperties();
    }
    
    private PropsUtils() {
        
    }

    /**
     * 获得配置的属性String值
     * @param name 属性名
     * @return 属性值
     */
    public static String get(String name) {
        return properties.getProperty(name);
    }

    /**
     * 获得配置的属性int值
     * @param name 属性名
     * @return 属性值
     */
    public static Integer getInt(String name) {
        String value = get(name);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }
    
    /**
     * 获得配置的属性int值。不存在返回默认值。
     * @param name 属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public static Integer getInt(String name, Integer defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : Integer.valueOf(value);
    }
    
    /**
     * 获得配置的属性Long值
     * @param name 属性名
     * @return 属性值
     */
    public static Long getLong(String name) {
        String value = get(name);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Long.valueOf(value);
    }
    
    /**
     * 获得配置的属性Long值。不存在返回默认值。
     * @param name 属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public static Long getLong(String name, Long defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : Long.valueOf(value);
    }
    
    /**
     * 获得配置的属性Double值
     * @param name 属性名
     * @return 属性值
     */
    public static Double getDouble(String name) {
        String value = get(name);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }
    
    /**
     * 获得配置的属性Double值。不存在返回默认值。
     * @param name 属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public static Double getDouble(String name, Double defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : Double.valueOf(value);
    }
    
    /**
     * 获得配置的属性boolean值
     * @param name 属性名
     * @return 属性值
     */
    public static boolean getBoolean(String name) {
        String value = get(name);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.parseBoolean(value);
    }
    
    /**
     * 获得配置的属性boolean值。不存在返回默认值。
     * @param name 属性名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public static boolean getBoolean(String name, boolean defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }
    
    /**
     * 获得配置的属性List值。字符串值以逗号分隔。
     * @param name 属性名
     * @return 属性List
     */
    public static List<String> getList(String name) {
        String[] values = StringUtils.split(get(name), ",");
        return Arrays.asList(values);
    }
    
    /**
     * 获得配置的属性Set值。字符串值以逗号分隔。
     * @param name 属性名
     * @return 属性Set
     */
    public static Set<String> getSet(String name) {
        String[] values = StringUtils.split(get(name), ",");
        Set<String> sets = new HashSet<String>();
        Collections.addAll(sets, values);
        return sets;
    }
}
