package com.java8.streams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
	
	@Test
	public void tst() {
		List<String> list = new ArrayList<>();
		list.add("37-48");
		list.add("37-48");
		list.add("37-48");
		list.add("37-48");
		list.add("13-24");
		list.add("25-36");
		list.add("0-12");
		list.add("0-12");
		list.add("0-12");
		list.add("49-60");
		list.add("61-72");
		list.add(">120");
		list.add("73-84");
		list.add("85-96");
		list.add("97-108");
		list.add("109-120");
		list.stream()
				.sorted((x, y) -> {
					int a = 0;
					int b = 0;
					if (x.contains("-")) {
						String s = x.split("-")[0];
						a = Integer.parseInt(s);
					}
					if (x.startsWith(">")) {
						a = Integer.parseInt(x.substring(1));
					}
					if (y.contains("-")) {
						String s1 = y.split("-")[0];
						b = Integer.parseInt(s1);
					}
					if (y.startsWith(">")) {
						b = Integer.parseInt(y.substring(1));
					}
					
					return a - b;
				})
				.forEach(System.out::println);
		
	}
	
	@Test
	public void testIntStream() {
		IntStream.range(1, 10)
				.forEach(System.out::println);
		System.out.println("********************");
		IntStream.rangeClosed(1, 10)
				.forEach(System.out::println);
	}
	
}

