package com.vteba.utils.date;

import java.util.Date;

import org.joda.time.DateTime;

/**
 * 基于JodaTime的一些时间工具类
 * @author yinlei 
 * date 2012-7-17 下午1:08:34
 */
public class JodaTimeUtils {
	
	/**
	 * 返回指定日期的当月最后一天
	 * @param date java.util.Date
	 * @author yinlei
	 * date 2012-7-17 下午12:51:28
	 */
	public static Date getLastDayOfMonth(Date date) {
		//12月份按后面处理会出错，所以单独处理
		DateTime dateTime = new DateTime(date);
		if (dateTime.getMonthOfYear() == 12) {
			DateTime dt = new DateTime(dateTime.getYear(), 12, 31, 0, 0, 0, 0);
			return dt.toDate();
		}
		DateTime dtr = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear() + 1, 1, 0, 0, 0, 0);
		dtr = dtr.plusDays(-1);
		return dtr.toDate();
	}
	
	/**
	 * 返回指定日期的当月最后一天
	 * @param date java.util.Date
	 * @author yinlei
	 * date 2012-7-17 下午12:51:28
	 */
	public static DateTime lastDayOfMonth(Date date) {
		//12月份按后面处理会出错，所以单独处理
		DateTime dateTime = new DateTime(date);
		if (dateTime.getMonthOfYear() == 12) {
			DateTime dt = new DateTime(dateTime.getYear(), 12, 31, 0, 0, 0, 0);
			return dt;
		}
		DateTime dtr = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear() + 1, 1, 0, 0, 0, 0);
		dtr = dtr.plusDays(-1);
		return dtr;
	}
	
	/**
	 * 返回指定日期的当月最后一天
	 * @param dateTime org.joda.time.DateTime
	 * @author yinlei
	 * date 2012-7-17 下午12:51:28
	 */
	public static Date getLastDayOfMonth(DateTime dateTime) {
		if (dateTime.getMonthOfYear() == 12) {
			DateTime dt = new DateTime(dateTime.getYear(), 12, 31, 0, 0, 0, 0);
			return dt.toDate();
		}
		DateTime dtr = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear() + 1, 1, 0, 0, 0, 0);
		dtr = dtr.plusDays(-1);
		return dtr.toDate();
	}
	
	/**
	 * 返回指定日期的当月最后一天
	 * @param dateTime org.joda.time.DateTime
	 * @author yinlei
	 * date 2012-7-17 下午12:51:28
	 */
	public static DateTime lastDayOfMonth(DateTime dateTime) {
		if (dateTime.getMonthOfYear() == 12) {
			DateTime dt = new DateTime(dateTime.getYear(), 12, 31, 0, 0, 0, 0);
			return dt;
		}
		DateTime dtr = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear() + 1, 1, 0, 0, 0, 0);
		dtr = dtr.plusDays(-1);
		return dtr;
	}
	
	/**
	 * 返回指定日期的当月第一天
	 * @param date java.util.Date
	 * @author yinlei
	 * date 2012-7-17 下午12:51:47
	 */
	public static Date getFirstDayOfMonth(Date date) {
		DateTime dateTime = new DateTime(date);
		DateTime dt = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0, 0);
		return dt.toDate();
	}
	
	/**
	 * 返回指定日期的当月第一天
	 * @param date java.util.Date
	 * @author yinlei
	 * date 2012-7-17 下午12:51:47
	 */
	public static DateTime firstDayOfMonth(Date date) {
		DateTime dateTime = new DateTime(date);
		DateTime dt = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0, 0);
		return dt;
	}
	
	/**
	 * 返回指定日期的当月第一天
	 * @param dateTime org.joda.time.DateTime
	 * @author yinlei
	 * date 2012-7-17 下午12:51:47
	 */
	public static Date getFirstDayOfMonth(DateTime dateTime) {
		DateTime dt = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0, 0);
		return dt.toDate();
	}
	
	/**
	 * 返回指定日期的当月第一天
	 * @param dateTime org.joda.time.DateTime
	 * @author yinlei
	 * date 2012-7-17 下午12:51:47
	 */
	public static DateTime firstDayOfMonth(DateTime dateTime) {
		DateTime dt = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0, 0);
		return dt;
	}
	
	/**
	 * 某一日期是否是当月最后一天
	 * @param dateTime org.joda.time.DateTime
	 * @return true or false
	 * @author yinlei
	 * date 2012-7-18 下午5:56:34
	 */
	public static boolean isLastDayOfMonth(DateTime dateTime) {
		return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth() == lastDayOfMonth(dateTime).getDayOfMonth();
	}
	
	/**
	 * 某一日期是否是当月最后一天
	 * @param date java.util.Date
	 * @return true or false
	 * @author yinlei
	 * date 2012-7-18 下午5:56:34
	 */
	public static boolean isLastDayOfMonth(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth() == lastDayOfMonth(dateTime).getDayOfMonth();
	}
	
	/**
	 * 某一日期是否是当月第一天
	 * @param date java.util.Date
	 * @author yinlei
	 * date 2012-7-18 下午5:58:41
	 */
	public static boolean isFirstDayOfMonth(Date date) {
		DateTime dateTime = new DateTime(date);
		return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth() == firstDayOfMonth(dateTime).getDayOfMonth();
	}
	
	/**
	 * 某一日期是否是当月第一天
	 * @param date org.joda.time.DateTime
	 * @author yinlei
	 * date 2012-7-18 下午5:58:41
	 */
	public static boolean isFirstDayOfMonth(DateTime dateTime) {
		return dateTime.dayOfMonth().withMaximumValue().getDayOfMonth() == firstDayOfMonth(dateTime).getDayOfMonth();
	}
	
	/**
	 * 获得当前会计期间的上一个会计期间
	 * @author yinlei
	 * date 2012-7-31 下午5:25:58
	 */
	public static String getLastPeriod(String period) {
		DateTime dateTime = new DateTime(period);
		dateTime = dateTime.minusMonths(1);//取上一个月
		return dateTime.toString("yyyy-MM");
	}
	
	/**
	 * 将日期date加year年
	 * @param date 某一日期
	 * @param year 年数
	 * @author yinlei
	 * date 2013-4-13 下午11:38:14
	 */
	public static Date addYears(Date date, int year) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusYears(year);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加month月
	 * @param date 某一日期
	 * @param month 月数
	 * @author yinlei
	 * date 2013-4-13 下午11:46:48
	 */
	public static Date addMonths(Date date, int month) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusMonths(month);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加week周
	 * @param date 某一日期
	 * @param week 周数
	 * @author yinlei
	 * date 2013-4-13 下午11:47:30
	 */
	public static Date addWeeks(Date date, int week) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusWeeks(week);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加day天
	 * @param date 某一日期
	 * @param day 天数
	 * @author yinlei
	 * date 2013-4-13 下午11:38:14
	 */
	public static Date addDays(Date date, int day) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusDays(day);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加hour小时
	 * @param date 某一日期
	 * @param hour 小时数
	 * @author yinlei
	 * date 2013-4-13 下午11:48:23
	 */
	public static Date addHours(Date date, int hour) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusHours(hour);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加minute分钟
	 * @param date 某一日期
	 * @param minute 分钟数
	 * @author yinlei
	 * date 2013-4-13 下午11:49:02
	 */
	public static Date addMinutes(Date date, int minute) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusMinutes(minute);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加second秒
	 * @param date 某一日期
	 * @param second 秒数
	 * @author yinlei
	 * date 2013-4-13 下午11:49:02
	 */
	public static Date addSeconds(Date date, int second) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusSeconds(second);
		return dateTime.toDate();
	}
	
	/**
	 * 将日期date加millis毫秒
	 * @param date 某一日期
	 * @param millis 毫秒数
	 * @author yinlei
	 * date 2013-4-13 下午11:49:02
	 */
	public static Date addMillis(Date date, int millis) {
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusMillis(millis);
		return dateTime.toDate();
	}
	
	public static void main(String[] args) {
		System.out.println(getFirstDayOfMonth(new Date()));
		System.out.println(getLastDayOfMonth(new Date()));
		DateTime dateTime = new DateTime();
		System.out.println(dateTime.getMonthOfYear());
		System.out.println(dateTime.getDayOfMonth());
		System.out.println((dateTime.dayOfMonth().withMaximumValue().getDayOfMonth()) ==lastDayOfMonth(new Date()).getDayOfMonth());
		System.out.println();
	}
}
