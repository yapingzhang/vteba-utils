package com.vteba.utils.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	 * 获得当前时间的字符串
	 * @param dateformat 时间格式
	 * @return date string
	 * @author yinlei
	 * date 2012-7-3 下午4:17:15
	 */
	public static String toDateString(String dateformat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        return dateFormat.format(new Date());
    }
	
	/**
	 * 获得指定时间指定格式的字符串
	 * @param date 要格式化的时间
	 * @param dateformat 时间格式
	 * @author yinlei
	 * date 2012-7-20 上午10:42:44
	 */
	public static String toDateString(Date date, String dateformat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
        return dateFormat.format(date);
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
