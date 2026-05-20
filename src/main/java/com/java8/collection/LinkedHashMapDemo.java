package com.java8.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于 LinkedHashMap 实现的 LRU 缓存
 */
public class LinkedHashMapDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
        map.put("key4", 4);
        System.out.println(map);
        System.out.println(map.getOrDefault("key1", 100));
        System.out.println(map);
        map.put("key2", 22);
        System.out.println(map);

    }
}
