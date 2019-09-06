package com.java8.string;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

	@Test
	public void tes() {
		String sql = "select \n" +
				"v.*\n" +
				"FROM\n" +
				"(\n" +
				"SELECT\n" +
				"v.PROVINCE_DEPT as provinceDept,\n" +
				"START_LONGITUDE AS startLng,\n" +
				"START_LATITUDE AS startLat,\n" +
				"END_LONGITUDE AS endLng,\n" +
				"END_LATITUDE AS endLat,\n" +
				"row_number() over (partition by v.PROVINCE_DEPT order by c.km desc) rn\n" +
				"FROM\n" +
				"IMPALA_VEH_DAY_SINGLE_RUN c,\n" +
				"SYS_VEHICLE_NATIONAL v\n" +
				"WHERE 1 = 1 \n" +
				"AND c.VID = V.UUID\n" +
				"AND c.km > 1\n" +
				"AND c.km < 200\n" +
				"AND\n" +
				"c.REPORT_DATE >= '2019-08-10' \n" +
				"AND\n" +
				"c.REPORT_DATE <= '2019-08-11' \n" +
				") v\n" +
				"where v.rn <= 3000;\n";
		System.out.println(sql);
	}

	@Test
	public void test22() {
		Map<String, String> map = new HashMap<>(20);
		// 充电开始时间
		int start = Integer.valueOf("0").intValue();
		// 充电结束时间
		int end = Integer.valueOf("2").intValue();
		System.out.println(start);
		System.out.println(end);

		IntStream.range(start, end + 1)
				// 时间段和充电的车进行映射存放
				.forEach(i -> map.put(String.valueOf(i >> 1), "i" + i));

		map.entrySet()
				.forEach(entyt -> System.out.println(entyt.getKey() + " : " + entyt.getValue()));
	}

}
