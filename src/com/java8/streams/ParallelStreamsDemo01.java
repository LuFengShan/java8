package com.java8.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
 * 并行流
 */
public class ParallelStreamsDemo01 {

  public static void main(String[] args) {
    int max = 1000000;
    List<String> values = new ArrayList<>(max);
    for (int i = 0; i < max; i++) {
      UUID uuid = UUID.randomUUID();
      values.add(uuid.toString());
    }

    // 单线程排序计数时间
    long startTime = System.nanoTime();
    long count = values.stream().sorted().count();
    System.out.println(count);
    long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    System.out.println(String.format("sequential sort took: %d ms", millis));
    // 多线程排序计数时间
    startTime = System.nanoTime();
    count = values.parallelStream().sorted().count();
    System.out.println(count);
    millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    System.out.println(String.format("sequential sort took: %d ms", millis));

  }

}

