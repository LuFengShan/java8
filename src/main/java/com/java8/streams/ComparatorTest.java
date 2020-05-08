package com.java8.streams;

import com.java8.function.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 比较器
 */
public class ComparatorTest {
	private static List<Person> people;

	@BeforeEach
	public void before() {
		people =
				Arrays.asList(
						new Person("la", "la1"),
						new Person("al", "al"),
						new Person("bl", "b2l"),
						new Person("s", "gx"),
						new Person("w", "hd3k"),
						new Person("lc", "lc"));
	}

	/**
	 * {@link Comparator#comparing(Function)},根据funciton功能接口返回的值来进行内部排序
	 * {@link Comparator#comparing(Function, Comparator)},排序的依据是function功能接口返回的值，comparator是自己指定的排序的实现
	 */
	@Test
	public void test(){
		// 1.1 要获取名字的排序
		Comparator<Person> comparator = Comparator.comparing(Person::getLastName); // 根据person的lastName属性进行排序
		people.sort(comparator);
		//people.forEach(System.out::println);
		// 1.2
		Comparator<Person> cmp = Comparator.comparing(
				Person::getLastName, // 根据person的lastName属性进行排序
				String.CASE_INSENSITIVE_ORDER); // 排序忽略大小写
		people.sort(comparator);
		//people.forEach(System.out::println);
		// 1.3
		Comparator<Person> comparing = Comparator.comparing(
				Person::getLastName, // 根据person的lastName属性进行排序
				Comparator.comparing(s -> s.length())); // 自定义排序规则（字符长度）
		people.sort(comparing.reversed()); // 返回一个比较器，它强制执行此比较器的反向排序。
		people.forEach(System.out::println);

	}

}
