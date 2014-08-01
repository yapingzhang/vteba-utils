package com.vteba.utils.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vteba.io.DefaultResourceLoader;
import com.vteba.io.Resource;
import com.vteba.io.ResourceLoader;

/**
 * Properties文件载入工具类。可载入多个properties文件。
 * 
 * <p>相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先。
 * 
 * @author yinlei
 * @date 2013-7
 */
public class PropertiesLoader {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	/**
     * 加载Properties属性文件，可以多个文件，格式可以如下：
     * <p><ul>
     * <li>必须支持全限定的 URLs，例如： "file:C:/test.dat".
     * <li>必须支持classpath 伪-URLs，例如 "classpath:test.dat".
     * <li>应该支持相对文件路径，例如 "WEB-INF/test.dat".
     * </ul>
     * @param resourcesPaths 资源文件的路径
     */
	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	/**
	 * 获取Properties，多个属性文件，都放在该实例中。
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 取出Property。
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return properties.getProperty(key);
	}

	/**
	 * 取出String类型的Property,如果都為Null则抛出异常
	 * @param key 属性key
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 取出String类型的Property.如果都為Null則返回Default值.
	 * @param key 属性key
	 * @param defaultValue 默认值
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property.如果都為Null或内容错误则抛出异常.
	 * @param key 属性key
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 * @param key 属性key
	 * @param defaultValue 默认值
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property.如果都為Null或内容错误则抛出异常.
	 * @param key 属性key
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 * @param key 属性key
	 * @param defaultValue 默认值
	 */
	public Double getDouble(String key, Double defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 * @param key 属性key
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Propert.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 * @param key 属性key
	 * @param defaultValue 默认值
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
     * 加载Properties属性文件，可以多个文件，格式可以如下：
     * <p><ul>
     * <li>必须支持全限定的 URLs，例如： "file:C:/test.dat".
     * <li>必须支持classpath 伪-URLs，例如 "classpath:test.dat".
     * <li>应该支持相对文件路径，例如 "WEB-INF/test.dat".
     * </ul>
     * @param resourcesPaths 资源文件的路径
     */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {
			logger.debug("Loading properties file from path:{}", location);
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:{}, {} ", location, ex.getMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}
}
