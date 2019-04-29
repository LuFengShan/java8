package com.java8.streams;

import java.util.HashMap;
import java.util.Map;

/*
 * map本身不支持流，java也没有提供流的方法，但是可以通过map.
 */
public class MapStreamsDemo01 {

  public static void main(String[] args) {
    Map<Integer, String> map = new HashMap<>();

    for (int i = 0; i < 10; i++) {
      map.putIfAbsent(i, "val" + i); //
    }

    // map.forEach((id, val) -> System.out.println(val));
    map.keySet().stream();

    map.computeIfPresent(3, (k, v) -> k * 10 + v); // 查找map中是不是有这个key,如果有，则替换map中的value
    System.out.println(map.get(3));

    map.remove(3, "val3");
    System.out.println(map.get(3));

    map.merge(3, "val3", (oldValue, newValue) -> oldValue.concat(newValue));// 如果这个值里面有这个key和value,则把新值和老值合并在一起，如果没有，则把新值存入map中
    System.out.println(map.get(3));
    map.merge(10, "val10", (oldValue, newValue) -> oldValue.concat(newValue));// 如果这个值里面有这个key和value,则把新值和老值合并在一起，如果没有，则把新值存入map中
    System.out.println(map.get(10));
  }

}

