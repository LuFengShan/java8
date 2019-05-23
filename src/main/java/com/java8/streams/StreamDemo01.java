package com.java8.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * 流的处理，流是中间的一个过程，没有结果，不是终端输出的东西,流不会操作原始的数据，只会操作中间的过程，其它的不改变
 */
public class StreamDemo01 {

	public static void main(String[] args) {
		List<String> stringCollection = new ArrayList<>();
		stringCollection.add("ddd2");
		stringCollection.add("aaa2");
		stringCollection.add("bbb1");
		stringCollection.add("aaa1");
		stringCollection.add("bbb3");
		stringCollection.add("ccc");
		stringCollection.add("bbb2");
		stringCollection.add("ddd1");

		// 过虑 Filter
		stringCollection.stream().filter(s -> s.startsWith("b")).sorted().forEach(System.out::println);
		System.out.println("************");
		// 排序 Sorted
		stringCollection.stream().sorted().filter(s -> s.endsWith("2")).forEach(System.out::println);
		System.out.println("************");
		// 中间转换 map
		stringCollection.stream().map(s -> s.toUpperCase()).sorted((a, b) -> a.compareTo(b))
				.forEach(System.out::println);
		System.out.println("************");
		// 匹配 match
		boolean allMatch = stringCollection.stream().allMatch(s -> s.length() > 2); // 全匹配
		System.out.println(allMatch);
		boolean anyMatch = stringCollection.stream().anyMatch(s -> Objects.equals(s, "ddd1")); // 匹配到任意一个
		System.out.println(anyMatch);
		boolean noneMatch = stringCollection.stream().noneMatch(s -> s.startsWith("z"));
		System.out.println(noneMatch);
		// 记数 count
		long count = stringCollection.stream().filter(s -> s.startsWith("d")).count();
		System.out.println(count);
		// 减少值 reduce
		Optional<String> reduce = stringCollection.stream().reduce((s1, s2) -> s1 + "#" + s2);
		reduce.ifPresent(System.out::println);


		stringCollection.stream()
				.flatMap(s -> Stream.of(s.split("")))
				.sorted()
				.collect(Collectors.groupingBy(s -> s))
				.entrySet()
				.stream()
				.forEach(e -> {
					System.out.println(e.getKey() + " : ");
					e.getValue()
							.forEach(System.out::print);
				});

		stringCollection.stream()
				.flatMap(s -> Stream.of(s.split("")))
				.sorted()
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()))
				.entrySet()
				.stream()
				.forEach(e -> System.out.println(e.getKey() + " : " + e.getValue()));
	}

}

