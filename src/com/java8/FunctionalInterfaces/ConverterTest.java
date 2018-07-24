package com.java8.FunctionalInterfaces;

public class ConverterTest {

  public static void main(String[] args) {
    // 实现这个功能的处理
    Converter<Integer, String> converter = String::valueOf;
    // 实现这个功能
    String string = converter.converter(123);
    System.out.println(string);


    // 实现这个功能的处理
    Converter<String, Integer> converter1 = Integer::valueOf;
    // 实现这个功能
    Integer integer = converter1.converter(converter.converter(456));
    System.out.println(integer);

    // 作用域
    // 您可以从本地外部作用域范围以及实例字段和静态变量访问最终作用域。
    final int num = 1;
    Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);
    System.out.println(stringConverter.converter(2));


    int num2 = 1;
    Converter<Integer, String> stringConverter1 = (from) -> String.valueOf(from + num2);
    System.out.println(stringConverter1.converter(2));

    // num2 = 4;编译不能通过，因为内部变量的局限性

    new ConverterTest().testScopes();

  }

  static int outerStaticNum;
  int outerNum;

  void testScopes() {
    Converter<Integer, String> stringConverter1 = (from) -> {
      outerNum = 23;
      return String.valueOf(from);
    };

    Converter<Integer, String> stringConverter2 = (from) -> {
      outerStaticNum = 72;
      return String.valueOf(from);
    };
  }
}

