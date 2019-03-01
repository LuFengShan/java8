package com.java8.concurrent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
	public static void main(String[] args) {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("1", "qq");
		map.put("2", "ww");
		map.put("3", "ee");
		map.put("4", "rr");
		

		Set<Map.Entry<String, String>> iterator = map.entrySet();
		for (Map.Entry<String, String> m : iterator) {
			System.out.println("key ：" + m.getKey() + ", value : " + m.getValue());
		}

	}
}
