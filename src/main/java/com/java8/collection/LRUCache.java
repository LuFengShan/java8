package com.java8.collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于 LinkedHashMap 实现的 LRU 缓存
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int maxCapacity;

    public LRUCache(int maxCapacity) {
        super(maxCapacity, 0.75f, true);
        this.maxCapacity = maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new LRUCache<>(4);
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);
        map.put("key4", 4);
        map.put("key5", 5);
        map.put("key6", 6);
        map.put("key7", 7);
        map.put("key8", 8);


        System.out.println(map);
        System.out.println(map.getOrDefault("key1", 100));
        System.out.println(map);
        map.put("key2", 22);
        System.out.println(map);

    }
}
