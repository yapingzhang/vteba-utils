package com.vteba.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

public class SystemUtils {

	public static final String MESSAGE_FILE = "message";
	public static final String APPLICATION_FILE = "application";

	/**
	 * 从message文件中取得消息
	 * 
	 * @param messageCode
	 *            消息号码
	 * @param args
	 *            参数数组
	 * @return
	 */
	public static String getAppMessage(String messageCode, Object[] args) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_FILE);
		String pattern = resourceBundle.getString(messageCode);

		if (pattern != null) {
			return MessageFormat.format(pattern, args);
		}
		return null;
	}

	/**
	 * 取得系统配置信息
	 * 
	 * @param configItem
	 *            配置项
	 * @return
	 */
	public static String getAppConfig(String configItem) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(APPLICATION_FILE);
		return resourceBundle.getString(configItem);
	}

	/**
	 * 创建指定目录
	 * 
	 * @param dir
	 *            目录路径
	 */
	public static void createDirectory(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		} else if (dir.exists() && !dir.isDirectory()) {
			dir.delete();
			dir.mkdirs();
		}
	}

	/**
	 * 列表对象深拷贝
	 * 
	 * @param <T>
	 *            列表实体类型
	 * @param src
	 *            拷贝源
	 * @return 拷贝目标
	 */
	public static <T> List<T> deepCopy(List<T> src) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			@SuppressWarnings("unchecked")
			List<T> dest = (List<T>) in.readObject();
			return dest;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
