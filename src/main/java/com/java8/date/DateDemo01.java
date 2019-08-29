package com.java8.date;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class DateDemo01 {

	public static void main(String[] args) {
		Clock clock = Clock.systemDefaultZone();
		long millis = clock.millis();
		Instant instant = clock.instant();
		Date from = Date.from(instant);
		System.out.println(from);

		int year = LocalDate.now().getYear();
		System.out.println("当前年份 ： " + year);

		System.out.println(LocalDate.now().getMonthValue());
		System.out.println(LocalDate.now());

		System.out.println(Integer.valueOf("06"));


		LocalDateTime now = LocalDateTime.now();
		System.out.println(now.getHour());
		System.out.println(now.getMinute());
		Long t = Long.valueOf(now.toString()
				.replace("-", "")
				.replace(":", "")
				.replace("T", "")
				.split("\\.")[0]);
		System.out.println(t);
		System.out.println(t - 600);
	}

	@Test
	public void localDate1() {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
		LocalDate yesterday = tomorrow.minusDays(2);
		LocalDate yesterday1 = today.minusDays(1);
		LocalDate yesterday2 = today.plusDays(-1);

		System.out.println(today);
		System.out.println(tomorrow);
		System.out.println(yesterday);
		System.out.println(yesterday1);
		System.out.println(yesterday2);

		LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
		DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
		// FRIDAY
		System.out.println(dayOfWeek);

		DateTimeFormatter germanFormatter =
				DateTimeFormatter
						.ofLocalizedDate(FormatStyle.MEDIUM)
						.withLocale(Locale.GERMAN);

		LocalDate xmas = LocalDate.parse("24.12.2014", germanFormatter);
		// 2014-12-24
		System.out.println(xmas);
	}

	@Test
	public void localDateTime1() {

		LocalDateTime sylvester = LocalDateTime.of(2014, Month.DECEMBER, 31, 23, 59, 59);

		DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
		// WEDNESDAY
		System.out.println(dayOfWeek);

		Month month = sylvester.getMonth();
		// DECEMBER
		System.out.println(month);

		long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
		// 1439
		System.out.println(minuteOfDay);

		Instant instant = sylvester
				.atZone(ZoneId.systemDefault())
				.toInstant();

		Date legacyDate = Date.from(instant);
		// Wed Dec 31 23:59:59 CET 2014
		System.out.println(legacyDate);


		DateTimeFormatter formatter =
				DateTimeFormatter
						.ofPattern("MMM dd, yyyy - HH:mm");

		LocalDateTime parsed = LocalDateTime.parse("12 03, 2014 - 07:13", formatter);
		String string = parsed.format(formatter);
		// Nov 03, 2014 - 07:13
		System.out.println(string);
	}

	@Test
	public void localTime1() {
		// get the current time
		Clock clock = Clock.systemDefaultZone();
		long t0 = clock.millis();
		System.out.println(t0);

		Instant instant = clock.instant();
		Date legacyDate = Date.from(instant);
		System.out.println(legacyDate);


		ZoneId zone1 = ZoneId.of("Europe/Berlin");
		ZoneId zone2 = ZoneId.of("Brazil/East");

		System.out.println(zone1.getRules());
		System.out.println(zone2.getRules());

		// time
		LocalTime now1 = LocalTime.now(zone1);
		LocalTime now2 = LocalTime.now(zone2);

		System.out.println(now1);
		System.out.println(now2);

		// false
		System.out.println(now1.isBefore(now2));

		long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
		long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
		System.out.println(hoursBetween);
		System.out.println(minutesBetween);


		// create time

		LocalTime now = LocalTime.now();
		System.out.println(now);

		LocalTime late = LocalTime.of(23, 59, 59);
		System.out.println(late);

		DateTimeFormatter germanFormatter =
				DateTimeFormatter
						.ofLocalizedTime(FormatStyle.SHORT)
						.withLocale(Locale.GERMAN);

		LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
		System.out.println(leetTime);
	}


}

