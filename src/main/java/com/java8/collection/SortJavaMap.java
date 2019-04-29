package com.java8.collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java 8 - Map按值，按键排序; 按自定义比较排序 - 降序和升序
 */
public class SortJavaMap {
	private static Map<String, Integer> numMap = new HashMap<>();

	@BeforeAll
	public static void testAddMap() {
		numMap.put("李白", 500);
		numMap.put("李世民", 1800);
		numMap.put("杜甫", 370);
		numMap.put("孙权", 2000);
		numMap.put("公孙策", 600);
	}

	@Test
	public void sortedByValues() {
		// .stream().sorted(Map.Entry.comparingByValue())按值排序
		Map<String, Integer> sortedByValues = numMap.entrySet()
				.stream()
				// 1.按值进行排序，默认是升序
				.sorted(Map.Entry.comparingByValue())
				// 2.使用LinkedHashMap来收集结果。因为，默认情况下会Collectors.toMap返回一个新的HashMap，但HashMap不保证迭代顺序
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		System.out.println("原始结果 ： " + numMap);
		// -> {孙权=2000, 李白=500, 杜甫=370, 李世民=1800, 公孙策=600}
		System.out.println("升序结果 ： " + sortedByValues);
		// -> {杜甫=370, 李白=500, 公孙策=600, 李世民=1800, 孙权=2000}

		Map<String, Integer> sortedByValuesReversed = numMap.entrySet()
				.stream()
				// 1.按值进行降序排序
				.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		System.out.println("降序结果 ： " + sortedByValuesReversed);
		// -> {孙权=2000, 李世民=1800, 公孙策=600, 李白=500, 杜甫=370}

	}

	@Test
	public void sortedByKeys() {

		System.out.println("原始结果 ： " + numMap);

		Map<String, Integer> sortedByValues = numMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				// 2.使用LinkedHashMap来收集结果。因为，默认情况下会Collectors.toMap返回一个新的HashMap，但HashMap不保证迭代顺序
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		System.out.println(sortedByValues);
		// {公孙策=600, 孙权=2000, 李世民=1800, 李白=500, 杜甫=370}
	}

	/**
	 * 自定义排序规则
	 */
	@Test
	public void sortedByCustomer() {
		Map<String, Customer> customerMap = new HashMap<>();
		customerMap.put("George", new Customer("Jack", 2100));
		customerMap.put("Oliver", new Customer("Jack", 5200));
		customerMap.put("Jack", new Customer("Jack", 4000));
		customerMap.put("Charlie", new Customer("Jack", 4500));
		customerMap.put("Harry", new Customer("Jack", 5600));

		final Map<String, Customer> sortedByValues = customerMap.entrySet()
				.stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		System.out.println("原始结果 ： " + customerMap);

		// -> {George=[name=Jack, salary=2100], Harry=[name=Jack, salary=5600], Oliver=[name=Jack, salary=5200],
		// Charlie=[name=Jack, salary=4500], Jack=[name=Jack, salary=4000]}

		System.out.println("升序结果 ： " + sortedByValues);
		// -> {George=[name=Jack, salary=2100], Jack=[name=Jack, salary=4000], Charlie=[name=Jack, salary=4500],
		// Oliver=[name=Jack, salary=5200], Harry=[name=Jack, salary=5600]}
	}
}


class Customer implements Comparable<Object> {
	String name;
	Integer salary;

	Customer(String name, int salary) {
		this.name = name;
		this.salary = salary; // 工资
	}

	@Override
	public int compareTo(Object o) {
		Customer e = (Customer) o;
		return e.salary.compareTo(salary);
	}

	public String toString() {
		return "[name=" + name + ", salary=" + salary + "]";
	}
}
