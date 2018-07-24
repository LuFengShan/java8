package com.java8.MethodConstructor;

public class SomethingTest {

  public static void main(String[] args) {
    // 静态的方法可以这样调用，但是不是静态的方法就要new了
    // Converter<String, String> converter = Something::startWiths;

    // 非静态访求的使用
    Something something = new Something();
    Converter<String, String> converter = something::startWiths;
    System.out.println(converter.converter("啊哈"));
  }

}

