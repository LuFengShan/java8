package com.java8.collection;

import com.java8.collection.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class Java8Collection {
	private static List<Person> people;

	@BeforeEach
	public void before() {
		people =
				Arrays.asList(
						new Person("石峰", "张", 30),
						new Person("lili", "luck", 20),
						new Person("老大", "张", 44),
						new Person("石头", null, 55),
						new Person("老二", "张", 28),
						new Person("汪", "王", 33),
						new Person(null, "孙", 22),
						new Person("力", "王", 35));
	}

	/**
	 * GroupingBy收集器用于按某些属性对对象进行分组，并将结果存储在Map实例中。
	 */
	@Test
	public void testGroupingBy() {
		// 1. 筛选出为不为null的对象
		List<Person> collect = people.stream().filter(Objects::nonNull).collect(Collectors.toList());
		//collect.forEach(System.out::println);
		// 2. 根据lastName分组，过虑掉lastName为null的
		Map<String, List<Person>> collect1 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName())) // 过虑lastName为null的
				.collect(Collectors.groupingBy(Person::getLastName));// 分组函数
		//collect1.entrySet().forEach(System.out::println);
		// 3.根据lastName分组，把组中的firstName用"、"拼接起来
		Map<String, String> collect2 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.mapping(Person::getFirstName, Collectors.joining("、"))
						)
				);
		collect2.entrySet().forEach(System.out::println);
		// 3.根据lastName分组，把组中的firstName用"、"拼接起来，并在拼接完成的字符串上面加上前后缀
		Map<String, String> collect3 = people.stream()
				.filter(person -> !Objects.equals(null, person.getLastName()))
				.collect(Collectors.groupingBy(Person::getLastName
						, Collectors.mapping(Person::getFirstName
								, Collectors.joining("、", "firstName: [", "]")
							)
						)
				);
		collect3.entrySet().forEach(System.out::println);

	}

	/**
	 * PartitioningBy是groupingBy的一个特殊情况，它接受Predicate实例并将Stream元素收集到Map实例中，该实例将布尔值存储为键，集合作为值存储。
	 * 在“true”键下，您可以找到与给定谓词匹配的元素集合，
	 * 在“false”键下，您可以找到与给定谓词不匹配的元素集合。
	 */
	@Test
	public void testPartitioningBy() {
		// 把lastName为null的分为一个组，其它的分为一个组
		Map<Boolean, List<Person>> collect = people.stream()
				.collect(Collectors.partitioningBy(person -> Objects.equals(null, person.getLastName())));
		collect.entrySet().forEach(System.out::println);
	}
}
