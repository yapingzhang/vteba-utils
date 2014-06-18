package com.vteba.utils.common;

import java.util.ArrayList;
import java.util.List;

public class ObjectUtils {
	
	/**
	 * 将对象数组中的null值去掉，重新返回一个不带null的object[]
	 * @param values 被转换的数组
	 * @author yinlei
	 * date 2012-6-24 下午5:24:43
	 */
	public static Object[] deleteNullValue(Object[] values){
		List<Object> list = new ArrayList<Object>();
		for(int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				list.add(values[i]);
			}
		}
		return list.toArray();
	}
	
	/**
	 * 在str后+%
	 * @param str
	 * @author yinlei
	 * date 2012-6-5 下午10:43:17
	 */
	public static String sqlLike(String str) {
		return str + "%";
	}
	
	/**
	 * 在字符串左侧，填充指定的字符
	 * @param source 源字符串
	 * @param fillChar 填充字符
	 * @param length 填充到的长度
	 * @author yinlei
	 * date 2012-7-5 下午10:48:45
	 */
	public static String fillLeft(String source, String fillChar, int length) {
		StringBuilder sb = new StringBuilder();
		if (source == null) {
			return "";
		}
		if (source.length() > length) {
			sb.append(source);
		} else {
			int len = source.length();
			while (sb.length() + len < length) {
				sb.append(fillChar);
			}
			sb.append(source);
		}
		return sb.toString();
	}
	
}
