package com.java8.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * <p>
 * Created by michael on 2019/3/11.
 */
public class LocalDateTimeUtils {


	/**
	 * 日期格式：yyyy-MM-dd
	 */
	public static String DATE_PATTERN = "yyyy-MM-dd";

	/**
	 * 日期格式：yyyyMMdd
	 */
	public static String DATE_PATTERN_A = "yyyyMMdd";

	/**
	 * 日期时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间格式(24小时制)：HH:mm:ss
	 */
	public static String TIME_PATTERN_24 = "HH:mm:ss";

	/**
	 * 时间格式(12小时制)：hh:mm:ss
	 */
	public static String TIME_PATTERN_12 = "hh:mm:ss";

	/**
	 * 获取年份
	 */
	public static void getYear() {
		LocalDateTime localTime = LocalDateTime.now();
		int year = localTime.get(ChronoField.YEAR);
		System.out.println(year);
	}

	/**
	 * 获取月份
	 */
	public static void getMonth() {
		LocalDateTime localTime = LocalDateTime.now();
		int month = localTime.get(ChronoField.MONTH_OF_YEAR);
		System.out.println(month);
	}

	/**
	 * 获取某月的第几天
	 */
	public static void getMonthOfDay() {
		LocalDateTime localTime = LocalDateTime.now();
		int day = localTime.get(ChronoField.DAY_OF_MONTH);
		System.out.println(day);
	}

	/**
	 * 格式化日期为字符串
	 *
	 * @param date    需要格式化的日期
	 * @param pattern 格式，如：yyyy-MM-dd
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 解析字符串日期为Date
	 *
	 * @param dateStr 日期字符串
	 * @param pattern 格式，如：yyyy-MM-dd
	 * @return Date
	 */
	public static Date parse(String dateStr, String pattern) {
		LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	/**
	 * 为Date增减分钟(减传负数)
	 *
	 * @param date    日期
	 * @param minutes 要增减的分钟数
	 * @return 新的日期
	 */
	public static Date addReduceMinutes(Date date, Long minutes) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime newDateTime = dateTime.plusMinutes(minutes);
		return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 增加时间
	 *
	 * @param date date
	 * @param hour 要增加的小时数
	 * @return 新的日期
	 */
	public static Date addHour(Date date, Long hour) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime localDateTime = dateTime.plusHours(hour);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 当天的起始时间
	 *
	 * @return 如：Tue Jun 11 00:00:00 CST 2019
	 */
	public static Date getStartTime() {
		LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		return localDateTime2Date(now);
	}

	/**
	 * 当天的结束时间
	 *
	 * @return 如：Tue Jun 11 23:59:59 CST 2019
	 */
	public static Date getEndTime() {
		LocalDateTime now = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999);
		return localDateTime2Date(now);
	}

	/**
	 * 减月份
	 *
	 * @param monthsToSubtract 月份
	 * @return Date
	 */
	public static Date minusMonths(long monthsToSubtract) {
		LocalDate localDate = LocalDate.now().minusMonths(monthsToSubtract);
		return localDate2Date(localDate);
	}

	/**
	 * LocalDate类型转为Date
	 *
	 * @param localDate
	 * @return
	 */
	public static Date localDate2Date(LocalDate localDate) {
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * LocalDateTime类型转为Date
	 *
	 * @param localDateTime
	 * @return Date
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * 查询当前年的第一天
	 *
	 * @param pattern 格式，如：yyyyMMdd
	 * @return 20190101
	 */
	public static String getFirstDayOfCurrentYear(String pattern) {
		LocalDateTime localDateTime = LocalDateTime.now().withMonth(1).withDayOfMonth(1);
		return format(localDateTime2Date(localDateTime), isEmpty(pattern) ? DATE_PATTERN_A : pattern);
	}

	public static String getFirstDayOfCurrentYear() {
		LocalDateTime localDateTime = LocalDateTime.now().withMonth(1).withDayOfMonth(1);
		return format(localDateTime2Date(localDateTime), DATE_PATTERN_A);
	}

	/**
	 * 查询前一年最后一个月第一天
	 *
	 * @param pattern 格式，如：yyyyMMdd
	 * @return 20190101
	 */
	public static String getLastMonthFirstDayOfPreviousYear(String pattern) {
		LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).withMonth(12).withDayOfMonth(1);
		return format(localDateTime2Date(localDateTime), isEmpty(pattern) ? DATE_PATTERN_A : pattern);
	}

	public static String getLastMonthFirstDayOfPreviousYear() {
		LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).withMonth(12).withDayOfMonth(1);
		return format(localDateTime2Date(localDateTime), DATE_PATTERN_A);
	}

	/**
	 * 查询前一年最后一个月的最后一天
	 *
	 * @param pattern 格式，如：yyyyMMdd
	 * @return 20190101
	 */
	public static String getLastMonthLastDayOfPreviousYear(String pattern) {
		LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).with(TemporalAdjusters.lastDayOfYear());
		return format(localDateTime2Date(localDateTime), isEmpty(pattern) ? DATE_PATTERN_A : pattern);
	}

	public static String getLastMonthLastDayOfPreviousYear() {
		LocalDateTime localDateTime = LocalDateTime.now().minusYears(1L).with(TemporalAdjusters.lastDayOfYear());
		return format(localDateTime2Date(localDateTime), DATE_PATTERN_A);
	}

	/**
	 * 获取当前日期
	 *
	 * @param pattern 格式，如：yyyy-MM-dd
	 * @return 2019-01-01
	 */
	public static String getCurrentDay(String pattern) {
		LocalDateTime localDateTime = LocalDateTime.now();
		return format(localDateTime2Date(localDateTime), isEmpty(pattern) ? DATE_PATTERN : pattern);
	}

	public static String getCurrentDay() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return format(localDateTime2Date(localDateTime), DATE_PATTERN);
	}

	/**
	 * 获取时区
	 */
	public static void getZone() {
		// 默认时区
		ZoneId zoneId = ZoneId.systemDefault();
		System.out.println(zoneId.toString());

		zoneId = ZoneId.of("Asia/Shanghai");
		System.out.println(zoneId.toString());

		zoneId = TimeZone.getTimeZone("CTT").toZoneId();
		System.out.println(zoneId.toString());
	}

	/**
	 * 字符串转时间
	 */
	public static void strToDate() {
		LocalDate date = LocalDate.parse("20190522", DateTimeFormatter.ofPattern(DATE_PATTERN_A));
		System.out.println(date);
	}

	/**
	 * 时间格式化输出
	 */
	public static void dateToStr() {
		LocalDate today = LocalDate.now();
		System.out.println("当前日期：" + today.format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
		System.out.println();

		LocalTime time = LocalTime.now();
		//24小时制
		System.out.println("当前时间(24小时制)：" + time.format(DateTimeFormatter.ofPattern(TIME_PATTERN_24)));
		//12小时制
		System.out.println("当前时间(12小时制)：" + time.format(DateTimeFormatter.ofPattern(TIME_PATTERN_12)));
		System.out.println();

		LocalDateTime now = LocalDateTime.of(today, time);
		//yyyyMMdd
		System.out.println(now.format(DateTimeFormatter.BASIC_ISO_DATE));
		//yyyy-MM-dd
		System.out.println(now.format(DateTimeFormatter.ISO_DATE));
		System.out.println();

		//2019-05-28T15:30:21.047
		System.out.println(now.format(DateTimeFormatter.ISO_DATE_TIME));
		//local date
		System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_DATE));
		//local time 带毫秒 eg:15:33:00.043
		System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_TIME));
		//local dateTime eg:2019-05-28T15:33:00.043
		System.out.println(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		//2019-W22-2
		System.out.println(now.format(DateTimeFormatter.ISO_WEEK_DATE));
	}

	/**
	 * Date 与 Localdatetime 的转换
	 */
	public static void transformWithDate() {
		// date 转 localdatetime
		Date date = new Date();
		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		System.out.println(localDateTime);
		// localdatetime 转 date
		Date date1 = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		System.out.println(date1);
	}

	/**
	 * 与时间戳的转换
	 */
	public static void transformWithTimestamp() {
		//秒级时间戳
		long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
		System.out.println("秒级时间戳:" + timeStamp);
		timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
		System.out.println("秒级时间戳:" + timeStamp);
		System.out.println("秒级时间戳转时间：" + Instant.ofEpochSecond(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime());
		System.out.println();
		//毫秒级时间戳
		timeStamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		System.out.println("毫秒级时间戳:" + timeStamp);
		System.out.println("毫秒级时间戳转时间：" + Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime());
	}

	/**
	 * 时间调整到特定某天
	 */
	public static void adjust() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println("当前时间：" + now);
		//同月的第一天
		LocalDateTime firstDayInSameMonthOfNow = now.withDayOfMonth(1);
		System.out.println("同月的第一天:" + firstDayInSameMonthOfNow);
		//同年的第一天
		LocalDateTime firstDayInSameYearOfNow = now.withDayOfYear(1);
		System.out.println("同年的第一天:" + firstDayInSameYearOfNow);
		//同年的第2月第10天
		LocalDateTime time = now.withMonth(2).withDayOfMonth(10);
		System.out.println("同年的第2月第10天:" + time);
		//当天的6点整
		time = now.withHour(6).withMinute(0).withSecond(0).withNano(0);
		System.out.println("当天的6点整:" + time);
	}

	public static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}

	/**
	 * 格式化时间-默认yyyy-MM-dd HH:mm:ss格式
	 *
	 * @param dateTime LocalDateTime对象
	 * @param pattern  要格式化的字符串
	 * @return
	 */
	public static String formatDateTime(LocalDateTime dateTime, String pattern) {
		if (dateTime == null) {
			return null;
		}
		return dateTime.format(DateTimeFormatter.ofPattern(isEmpty(pattern) ? DATE_TIME_PATTERN : pattern));
	}

	public static String formatDateTime(LocalDateTime dateTime) {
		return formatDateTime(dateTime, DATE_TIME_PATTERN);
	}

	/**
	 * 获取某天的00:00:00
	 *
	 * @param dateTime
	 * @return
	 */
	public static String getDayStart(LocalDateTime dateTime) {
		return formatDateTime(dateTime.with(LocalTime.MIN));
	}

	public static String getDayStart() {
		return getDayStart(LocalDateTime.now());
	}

	/**
	 * 获取某天的23:59:59
	 *
	 * @param dateTime
	 * @return
	 */
	public static String getDayEnd(LocalDateTime dateTime) {
		return formatDateTime(dateTime.with(LocalTime.MAX));
	}

	public static String getDayEnd() {
		return getDayEnd(LocalDateTime.now());
	}

	/**
	 * 获取某月第一天的00:00:00
	 *
	 * @param dateTime LocalDateTime对象
	 * @return
	 */
	public static String getFirstDayOfMonth(LocalDateTime dateTime) {
		return formatDateTime(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
	}

	public static String getFirstDayOfMonth() {
		return getFirstDayOfMonth(LocalDateTime.now());
	}

	/**
	 * 获取某月最后一天的23:59:59
	 *
	 * @param dateTime LocalDateTime对象
	 * @return
	 */
	public static String getLastDayOfMonth(LocalDateTime dateTime) {
		return formatDateTime(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));
	}

	public static String getLastDayOfMonth() {
		return getLastDayOfMonth(LocalDateTime.now());
	}

}
