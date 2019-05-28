package com.java8.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lambda01 {

  public static void main(String[] args) {
    List<String> list = Arrays.asList("lili", "nana", "zeze", "dandan", "pangpang", "lala");
    String l = list.stream()
            .filter(s -> s.startsWith("l"))
            .findFirst()
            .get();
    System.out.println(l);


    list.stream().filter(s -> s.startsWith("n")).forEach(System.out::println);

    // 老的写法
    Collections.sort(list, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    });

    list.stream().forEach(System.out::println);
    // 新的写法
    Collections.sort(list, (String a, String b) -> {
      return a.length() - b.length();
    });
    list.stream().forEach(System.out::println);
    // 新的写法
    Collections.sort(list, (a, b) -> a.length() - b.length());
    list.stream().forEach(System.out::println);

    list.sort((a, b) -> a.compareTo(b));
    list.sort(Comparator.naturalOrder());
  }

}

