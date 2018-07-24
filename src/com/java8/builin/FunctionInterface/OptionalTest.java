package com.java8.builin.FunctionInterface;

import java.util.Optional;

public class OptionalTest {

  public static void main(String[] args) {
    Optional<String> optional = Optional.of("bam");

    optional.isPresent(); // true
    optional.get(); // "bam"
    optional.orElse("fallback"); // "bam"

    optional.ifPresent((s) -> System.out.println(s.charAt(0))); // "b"

    Optional<Integer> optional2 = Optional.of(255);
    System.out.println(optional2.isPresent());
    System.out.println(optional2.orElse(200));
    System.out.println(optional2.get());
  }

}

