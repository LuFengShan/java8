package com.java8.date;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

public class DateDemo02 {

	@Test
	public void demoLocalDate() {
		// 指定一个日期, 方式1
		LocalDate localDate = LocalDate.of(2019, 9, 1);
		System.out.println(localDate);
		// 指定一个日期, 方式2
		LocalDate parseDay = LocalDate.parse("2019-10-02");
		System.out.println(parseDay);
		System.out.println(localDate + " == " + parseDay + "? : " + localDate.equals(parseDay));

		// 月份中的第几天
		int dayOfMonth = localDate.getDayOfMonth();
		System.out.println(localDate + " 月份中的第几天: " + dayOfMonth);
		// 一周的第几天
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		System.out.println(localDate + " 所在周中的第几天: " + dayOfWeek + ":" + dayOfWeek.getValue());
		// 月份的天数
		int lengthOfMonth = localDate.lengthOfMonth();
		System.out.println(localDate + " 所在月有多少天: " + lengthOfMonth);
		// 是否为闰年
		boolean leapYear = localDate.isLeapYear();
		System.out.println(localDate + " 是否为闰年: " + leapYear);

		// 获取指定日期的那一天的开始时间
		System.out.println(localDate + " 这天的开始时间: " + localDate.atStartOfDay());
		// 获取指定日期所在月的第一天的日期
		System.out.println(localDate + " 这天所在月的第一天的日期: " + localDate.with(TemporalAdjusters.firstDayOfMonth()));

		// 比较一个前面这个日期是否在后面的一个日期的前面
		boolean notBefore = LocalDate.parse("2019-10-01").isBefore(LocalDate.parse("2019-10-02"));
		System.out.println(notBefore);
		// 比较一个前面这个日期是否在后面的日期的后面
		boolean isAfter = LocalDate.parse("2019-10-01").isAfter(LocalDate.parse("2019-10-02"));
		System.out.println(isAfter);

		// 日期加减，年月类似
		System.out.println(localDate + "日期加一天：" + localDate.plusDays(1L));
		System.out.println(localDate + "日期减一天：" + localDate.minusDays(1L));

	}

	@Test
	public void demoLocalTime() {
		// LocalTime获得的时间格式为：11:41:58.904。也就是，HH:mm:ss.nnn，这里nnn是纳秒
		LocalTime now = LocalTime.now();
		System.out.println(now);
		// 指定一个时间, 方式1
		LocalTime localTime = LocalTime.of(10, 29, 55);
		System.out.println(localTime);
		// 指定一个时间, 方式2
		LocalTime parseTime = LocalTime.parse("13:45:50");
		System.out.println(parseTime);

		// 时间区间的最大值 23:59:59.999999999
		System.out.println(LocalTime.MAX);
		// 时间区间的最小值 00:00
		System.out.println(LocalTime.MIN);

		/**
		 * 时间的其它的操作和日期LocalDate的类似
		 */

	}

	/**
	 * LocalDateTime表示日期和时间组合。可以通过of()方法直接创建，也可以调用LocalDate的atTime()方法或LocalTime的atDate()方法将LocalDate或LocalTime合并成一个LocalDateTime。
	 */
	@Test
	public void demoLocalDateTime() {
		// 2019-10-31T14:56:14.868 格式
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);

		// 指定一个日期时间, 方式1
		LocalDateTime localDateTime = LocalDateTime.of(2019, 10, 31, 9, 29, 55);
		System.out.println(localDateTime);
		// 指定一个日期时间, 方式2, 注意这个日期和时间之间要有"T"
		LocalDateTime parse = LocalDateTime.parse("2019-09-30T09:30:55");
		System.out.println(parse);

		// 拼接日期
		LocalTime localTime = LocalTime.of(10, 29, 55);
		LocalDate localDate = LocalDate.of(2018, 10, 31);
		LocalDateTime localDateTime1 = localDate.atTime(localTime);
		LocalDateTime localDateTime2 = localTime.atDate(localDate);
		System.out.println(localDateTime1 + " : " + localDateTime1 + " : " + (localDateTime1.equals(localDateTime2)));
	}

	/**
	 * Instant用于一个时间戳，与System.currentTimeMillis()类似，但Instant可以精确到纳秒（Nano-Second）
	 */
	@Test
	public void demoInstant() {
		// 当前的时间戳
		Instant now = Instant.now();
		System.out.println(now);
		// 其中ofEpochSecond第一个参数表示秒，第二个参数表示纳秒。整体表示：从1970-01-01 00:00:00开始后的365天100纳秒的时间点
		Instant ofEpochSecond = Instant.ofEpochSecond(365 * 24 * 60, 100);
		System.out.println(ofEpochSecond);

	}

	/**
	 * Duration的内部实现与Instant类似，但Duration表示时间段，通过between方法创建。
	 */
	@Test
	public void demoDuration() {
		LocalDateTime from = LocalDateTime.now();
		LocalDateTime to = LocalDateTime.now().plusDays(1);
		Duration duration = Duration.between(from, to);

		// 区间统计换算
		// 总天数
		long days = duration.toDays();
		System.out.println(days);
		// 小时数
		long hours = duration.toHours();
		System.out.println(hours);
		// 分钟数
		long minutes = duration.toMinutes();
		System.out.println(minutes);
		// 秒数
		long seconds = duration.getSeconds();
		System.out.println(seconds);
		// 毫秒数
		long milliSeconds = duration.toMillis();
		System.out.println(milliSeconds);
		// 纳秒数
		long nanoSeconds = duration.toNanos();
		System.out.println(nanoSeconds);

		// Duration对象还可以通过of()方法创建，该方法参数为时间段长度和时间单位。

		// 7天
		Duration duration1 = Duration.of(7, ChronoUnit.DAYS);
		System.out.println(duration1);
		// 60秒
		Duration duration2 = Duration.of(60, ChronoUnit.SECONDS);
		System.out.println(duration2);
	}

	/**
	 * Period与Duration类似，获取一个时间段，只不过单位为年月日，也可以通过of方法和between方法创建，between方法接收的参数为LocalDate。
	 */
	@Test
	public void demoPeriod() {
		Period period = Period.of(1, 10, 25);
		System.out.println(period);

		Period period1 = Period.between(LocalDate.now(), LocalDate.now().plusYears(1));
		System.out.println(period1);
	}

	/**
	 * ZonedDateTime类，用于处理带时区的日期和时间。ZoneId表示不同的时区。大约有40不同的时区
	 */
	@Test
	public void demoZonedDateTime() {
		// 获取所有时区集合：
		Set allZoneIds = ZoneId.getAvailableZoneIds();
		System.out.println(allZoneIds.size());

		// 创建时区
		ZoneId zoneId = ZoneId.of("Asia/Shanghai");
		System.out.println(zoneId.getId());

		// 把LocalDateTime转换成特定的时区：
		ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), zoneId);
		System.out.println(zonedDateTime);
		System.out.println(zonedDateTime.toLocalDate());
		System.out.println(zonedDateTime.toLocalDateTime());
		System.out.println(zonedDateTime.toLocalTime());
		System.out.println(zonedDateTime.toOffsetDateTime());
	}

	@Test
	public void demoDateTimeFormatter() {
		LocalDateTime dateTime = LocalDateTime.of(2020, 8, 28, 0, 0, 0);
		String str = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		System.out.println(str);
		str = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		System.out.println(str);
		str = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println(str);
		str = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		System.out.println(str);
	}

	@Test
	public void InstantMapperToLocalDateTime() {

		//Instant:时间戳（以Unix元年：1970年1月1日 00:00:00到某个时间之间的毫秒值）
		Instant ins = Instant.now(); //默认获取的是UTC时区
		System.out.println(ins);
		System.out.println(ins.toEpochMilli()); //ms

		Instant timestamp = Instant.ofEpochMilli(ins.toEpochMilli());
		ZonedDateTime losAngelesTime = timestamp.atZone(ZoneId.of("Asia/Shanghai"));
		LocalDateTime localDateTime = losAngelesTime.toLocalDateTime();
		System.out.println(localDateTime);


		timestamp = Instant.ofEpochMilli(1596617648000L);
		losAngelesTime = timestamp.atZone(ZoneId.of("Asia/Shanghai"));
		localDateTime = losAngelesTime.toLocalDateTime();
		String str = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
		System.out.println(str);

		// 另一种格式转换方式
		String inputValue = "2012-08-15T22:56:02.038Z";
		timestamp = Instant.parse(inputValue);
		losAngelesTime = timestamp.atZone(ZoneId.of("America/Los_Angeles"));
		System.out.println(losAngelesTime.toLocalDateTime());
	}

	@Test
	public void localtime() {
		LocalTime localTime = LocalTime.now();
		LocalTime futureTime = LocalTime.of(18, 0);
		LocalTime futureTime2 = LocalTime.of(18, 1);
		System.out.println("localTime:" + localTime);
		System.out.println("futureTime:" + futureTime);
		System.out.println(localTime.isBefore(futureTime));
		System.out.println(localTime.compareTo(futureTime));
		System.out.println(futureTime2.isAfter(futureTime));
	}
}