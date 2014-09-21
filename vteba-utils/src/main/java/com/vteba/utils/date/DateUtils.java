package com.vteba.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	/**年月日时分秒，以-连接*/
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	/**年月日时分，以-连接*/
	public static final String YMDHM = "yyyy-MM-dd HH:mm";
	/**年月日时，以-连接*/
	public static final String YMDH = "yyyy-MM-dd HH";
	/**年月日，以-连接*/
	public static final String YMD = "yyyy-MM-dd";
	/**年月，以-连接*/
	public static final String YM = "yyyy-MM";
	/**时分秒*/
	public static final String HMS = "HH:mm:ss";
	/**时分*/
	public static final String HM = "HH:mm";
	/**年月日时分秒*/
	public static final String SYMDHMS = "yyyyMMdd HH:mm:ss";
	/**年月日时分*/
	public static final String SYMDHM = "yyyyMMdd HH:mm";
	/**年月日时*/
	public static final String SYMDH = "yyyyMMdd HH";
	/**年月日*/
	public static final String SYMD = "yyyyMMdd";
	/**年月*/
	public static final String SYM = "yyyyMM";
	
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS);
        return dateFormat.format(date);
    }
	
	/**
     * 将unix时间戳格式化，默认格式是：yyyy-MM-dd HH:mm:ss。
     * @param date 要格式化的日期Unix时间戳
     * @return 日期字符串
     */
	public static String toDateString(Long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS);
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
        SimpleDateFormat format = new SimpleDateFormat(HMS);
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
        String current = toDateString(YMD);
        String dateString = toDateString(date, YMD);
        if (dateString != null && current.equals(dateString)) {
            return true;
        }
        return false;
    }
    
    /**
     * 获得两个时间的月数差，格式化到月份，然后相减。精确的。
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static int monthDiffer(Date start, Date end) {
    	int startMonth = Integer.parseInt(DateUtils.toDateString(start, SYM));
    	int endMonth = Integer.parseInt(DateUtils.toDateString(end, SYM));
    	int diff = endMonth - startMonth;
        return diff;
    }
    
    /**
     * 获得两个时间的月数差，每月天数按30天算
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long monthDiff(Date start, Date end) {
    	long diff = end.getTime() - start.getTime();
        long month = diff / (1000 * 60 * 60 * 24 * 30);
        return month;
    }
    
    /**
     * 获得两个时间的天数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long dayDiff(Date start, Date end) {
    	long diff = end.getTime() - start.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }
    
    /**
     * 获得两个时间的小时数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long hourDiff(Date start, Date end) {
		long hour = minDiff(start, end) / 60;
		return hour;
    }
    
    /**
     * 获得两个时间的分钟数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long minDiff(Date start, Date end) {
		long min = secDiff(start, end) / 60;
		return min;
    }
    
    /**
     * 获得两个时间的秒数差
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差
     */
    public static long secDiff(Date start, Date end) {
		long l = end.getTime() - start.getTime();
		return l / 1000;
    }
    
    public static Date getNow() {
    	return new Date();
    }
    
    /**
     * 获得某一过去时间距离现在的月数差，每月天数按30天算
     * @param date 某一过去时间
     * @return 月数差
     */
    public static long monthDiff(Date date) {
    	long diff = getNow().getTime() - date.getTime();
        long month = diff / (1000 * 60 * 60 * 24 * 30);
        return month;
    }
    
    /**
     * 获得某一过去时间距离现在的天数差
     * @param date 某一过去时间
     * @return 天数差
     */
    public static long dayDiff(Date date) {
    	long diff = getNow().getTime() - date.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }
    
    /**
     * 获得某一过去时间距离现在的小时数差
     * @param date 某一过去时间
     * @return 小时数差
     */
    public static long hourDiff(Date date) {
		long hour = minDiff(date) / 60;
		return hour;
    }
    
    /**
     * 获得某一过去时间距离现在的分钟数差
     * @param date 某一过去时间
     * @return 分钟数差
     */
    public static long minDiff(Date date) {
		long min = secDiff(date) / 60;
		return min;
    }
    
    /**
     * 获得某一过去时间距离现在的秒数差
     * @param date 某一过去时间
     * @return 秒数差
     */
    public static long secDiff(Date date) {
		long l = getNow().getTime() - date.getTime();
		return l / 1000;
    }
}
