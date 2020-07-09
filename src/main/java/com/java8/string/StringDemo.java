package com.java8.string;

import com.alibaba.fastjson.JSON;
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

	@Test
	public void testc(){
		String str = "0.3682_1.0588_-0.5869_-0.5473_0.1765_0.3015_-0.434_0.7884_0.4992_0.7487_-0.1397_0.2432_0.9142_-2.2131_1.0858_1.0266_-0.6108_-1.1997_0.9608_-0.3757_1.3428_-0.434_-0.3128_-1.7622_-0.2311_0.3211_0.0986_-1.4138_-1.1983_0.0049_1.4477_-0.2311_0.3794_0.8154_0.6125_-0.822_-0.1374_0.1974_-0.2311_0.3015_-0.0595_-0.1262_-0.9666_-0.8304_-0.8835_0.8933_-0.3113_0.9599_0.4325_-0.5838_-0.2311_1.0649_0.9599_1.4281_-5.2467_-1.258_-0.8356_-0.8565_-0.8835_0.3075_-0.3757_1.5457_0.7884_0.8154_0.6904_0.4409_-0.2978_-0.1262_-0.5202_1.5061_0.8154_0.9016_-1.2393_-0.7119_-0.4423_0.8153_-0.4027_-0.8491_1.3615_0.9329_-0.3696_0.9921_-0.2311_0.4349_-0.634_1.0266_1.1045_-1.0924_1.4281_-0.1532_0.3015_-1.8498_-0.0865_-0.1644_-1.1008_0.96";
		List<String> collect = Pattern.compile("_")
				.splitAsStream(str)
				.collect(Collectors.toList());
		System.out.println(JSON.toJSONString(collect));
	}
	
	@Test
	public void testNewString() {
		String s1 = new String("sgxmmhh");
	}

}
