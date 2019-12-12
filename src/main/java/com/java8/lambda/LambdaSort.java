package com.java8.lambda;

import org.junit.jupiter.api.Test;

import java.util.*;

public class LambdaSort {

	@Test
	public void test1() {
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

	@Test
	public void test2() {
		List<Column3> list = new ArrayList<>();
		list.add(Column3.builder().col1("aa").col2("zhangsan").col3("25").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("zhangsan").col3("25").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("lisi").col3("10").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("wangwu").col3("17").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("zhaoliu").col3("18").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("heige").col3("25").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("songjiang").col3("25").build());
		list.add(Column3.builder().col1(UUID.randomUUID().toString().substring(0, 6)).col2("ligui").col3("20").build());
		// stream自定义排序
		list.stream()
				.sorted(Comparator.comparing(Column3::getCol3, Comparator.comparingInt(Integer::parseInt)))
				.forEach(System.out::println);
		System.err.println("-----------------");
		list.stream()
				.sorted(Comparator.comparing(Column3::getCol3, Comparator.comparingInt(Integer::parseInt)).reversed())
				.forEach((System.err::println));
	}

}

