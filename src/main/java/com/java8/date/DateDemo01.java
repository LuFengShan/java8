package com.java8.date;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public class DateDemo01 {

  public static void main(String[] args) {
    Clock clock = Clock.systemDefaultZone();
    long millis = clock.millis();
    Instant instant = clock.instant();
    Date from = Date.from(instant);
    System.out.println(from);
  }

}

