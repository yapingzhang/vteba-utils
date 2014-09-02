package com.vteba.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
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
	
	/**
	 * 将日期格式化，默认格式是：yyyy-MM-dd HH:mm:ss。
	 * @param date 要格式化的日期
	 * @return 日期字符串
	 */
	public static String toDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
	
	/**
     * 将unix时间戳格式化，默认格式是：yyyy-MM-dd HH:mm:ss。
     * @param date 要格式化的日期Unix时间戳
     * @return 日期字符串
     */
	public static String toDateString(Long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(date));
    }
	
	/**
	 * 解析日期字符串为日期对象。默认格式是 yyyy-MM-dd
	 * @param date 被解析的日期字符串
	 * @return 日期
	 */
	public static Date parseDate(String date) {
	    SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance();
	    try {
            Date newDate = format.parse(date);
            return newDate;
        } catch (ParseException e) {
            
        }
	    return null;
	}
	
	/**
     * 解析日期字符串为日期对象。
     * @param date 被解析的日期字符串
     * @param pattern 日期格式 如：yyyy-MM-dd HH:mm:ss
     * @return 日期
     */
	public static Date parseDate(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date newDate = format.parse(date);
            return newDate;
        } catch (ParseException e) {
            
        }
        return null;
    }
	
	/**
	 * 默认使用日期格式 yyyy-MM-dd HH:mm:ss，解析日期字符串datetime。
	 * 如果日期字符串不全，那么解析出来的日期也会不全。
	 * @param datetime 日期字符串
	 * @return 解析后的日期对象
	 */
	public static Date parseDateTime(String datetime) {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        try {
            Date newDate = format.parse(datetime);
            return newDate;
        } catch (ParseException e) {
            
        }
        return null;
    }
	
	/**
	 * 解析时间为日期对象，日期对象是 1970年1月1号。格式是 HH:mm:ss
	 * @param time 时间 
	 * @return 日期对象
	 */
	public static Date parseTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date newDate = format.parse(time);
            return newDate;
        } catch (ParseException e) {
            
        }
        return null;
    }
	
	/**
     * 精确到天，看是否是当天
     * @param date 日期
     * @return 当天返回true，否则false
     */
    public static boolean isToday(Date date) {
        String current = toDateString("yyyy-MM-dd");
        String dateString = toDateString(date, "yyyy-MM-dd");
        if (dateString != null && current.equals(dateString)) {
            return true;
        }
        return false;
    }
}
