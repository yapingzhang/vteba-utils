package com.vteba.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
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
}
