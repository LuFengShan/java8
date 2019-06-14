package com.java8.date;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    System.out.println(now.parse("12:30", DateTimeFormatter.ofPattern("HH:mm")));
  }

}

