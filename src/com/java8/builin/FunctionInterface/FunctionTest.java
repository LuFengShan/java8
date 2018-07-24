package com.java8.builin.FunctionInterface;

import java.util.function.Function;

public class FunctionTest {

  public static void main(String[] args) {
    Function<String, Integer> stringConverterInteger = Integer::valueOf;
    System.out.println(stringConverterInteger.apply("123"));

    Function<String, Integer> integerConverterString =
        stringConverterInteger.andThen(Integer::valueOf);
    System.out.println(integerConverterString.apply("456"));
  }

}

