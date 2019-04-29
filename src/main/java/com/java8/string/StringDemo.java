package com.java8.string;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class StringDemo {
	public static void main(String[] args) {
		// 任意数量的字符串连接到具有给定分隔符的单个字符串
		String join = String.join(":", "name", "guang", "age");// => foobar:foo:bar
		System.out.println(join);

		// 也可以把字符集合中的字符给拼接起来
		List<String> strings = new LinkedList<>();
		strings.add("Java");
		strings.add("is");
		strings.add("cool");
		String message = String.join(" ", strings); // "Java is cool"
		System.out.println(message);

		Set<String> strings1 = new LinkedHashSet<>();
		strings1.add("Java");
		strings1.add("is");
		strings1.add("very");
		strings1.add("cool");
		String message1 = String.join("-", strings); // "Java-is-very-cool"
		System.out.println(message1);

		// chars为字符串的所有字符创建一个流
		String str = "dklja:孙:wang";
		String collect = str.chars()
				.distinct()
				.mapToObj(c -> String.valueOf((char) c))
				.sorted()
				.collect(Collectors.joining());
		System.out.println(collect);

		// 不仅字符串而且正则表达式模式都受益于流。我们可以为任何模式拆分字符串，并创建一个要处理的流，而不是将字符串拆分为每个字符的流
		String bar = Pattern.compile(":") // 传入正则表达式
				.splitAsStream("foobar:foo:bar") // 根据正则表达式，把字符拆分为foobar,foo,bar三个子字符串
				.filter(s -> s.contains("bar"))
				.sorted()
				.collect(Collectors.joining(":"));// bar:foobar
		System.out.println(bar);

		// 可以将正则表达式模式转换为谓词。例如，这些谓词可用于过滤字符串流：
		Pattern pattern = Pattern.compile(".*@qq\\.com");
		long count = Stream.of("58298932@gmail.com", "66897825@hotmail.com", "google@qq.com")
				.filter(pattern.asPredicate())
				.count();
		System.out.println(count); // 1

		//
		

	}
}
