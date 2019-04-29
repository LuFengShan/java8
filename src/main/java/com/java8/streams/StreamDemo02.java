package com.java8.streams;

import java.util.stream.Stream;

public class StreamDemo02 {
  public static void main(String[] args) {
    // Arrays.asList("a1", "a2", "a3").stream().findFirst().ifPresent(System.out::println); // a1
    // Stream.of("a1", "a2", "a3").findFirst().ifPresent(System.out::println);
    //
    // IntStream intStream = IntStream.range(10, 15);
    // intStream.filter(i -> i % 2 == 0).map(i -> i * 2).average().ifPresent(System.out::println);

    // Stream.of("d2", "a2", "b1", "b3", "c3").filter(s -> {
    // System.out.println("filter: " + s);
    // return true;
    // }).anyMatch(s -> {
    // System.out.println("anyMatch: " + s);
    // return s.startsWith("a");
    // });

    // Stream.of("d2", "a2", "b1", "b3", "c")
    // .filter(s -> {
    // System.out.println("filter: " + s);
    // return s.startsWith("a");
    // })
    // .map(s -> {
    // System.out.println("map: " + s);
    // return s.toUpperCase();
    // })
    // .forEach(s -> System.out.println("forEach: " + s));

    Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> {
      System.out.println("filter: " + s);
      return s.startsWith("a");
    }).map(s -> {
      System.out.println("map: " + s);
      return s.toUpperCase();
    }).forEach(s -> System.out.println("forEach: " + s));

  }
}

