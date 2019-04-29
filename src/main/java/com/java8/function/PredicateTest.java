package com.java8.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicate Predicate< T > 接收T对象并返回boolean
 *
 * <ul>
 * <li>表示一个参数的谓词（布尔值函数）。 这是一个功能接口，其功能方法是test（Object）。
 * <li>作用:判断对象是否符合某个条件
 * <li>使用场景:ArrayList的removeIf(Predicate)：删除符合条件的元素
 * <p>​ 如果条件硬编码在ArrayList中，它将提供无数的实现，但是如果让调用者传入条件，这样ArrayList就可以从复杂和无法猜测的业务中解放出来。
 * <li>设计思想 提取条件，让条件从处理逻辑脱离出来，解耦合
 * </ul>
 */
public class PredicateTest {
	private static List<Person> people;

	@BeforeEach
	public void before() {
		people =
				Arrays.asList(
						new Person("la", "la"),
						new Person("al", "al"),
						new Person("bl", "bl"),
						new Person("s", "gx"),
						new Person("w", "hd"),
						new Person("lc", "lc"));
	}

	/**
	 *
	 */
	@Test
	public void testTest() {
		// 1.制定提取条件：如果firstName包含开头"l",则返回true，否则返回false.
		Predicate<Person> personPredicate =
				person -> {
					if (person.firstName.startsWith("l")) {
						System.out.println(person);
						return true;
					}
					return false;
				};
		// 2. 执行
		people.forEach(person -> personPredicate.test(person));
	}

	/**
	 * {@link Predicate#and(Predicate)}. 主要是测试and方法：返回一个组合谓词，表示此谓词和另一个谓词的短路逻辑AND。
	 * 在评估组合谓词时，如果此谓词为false，则不评估其他谓词。
	 * {@link Predicate#or(Predicate)}. 主要是测试and方法：返回一个组合谓词，表示此谓词和另一个谓词的短路逻辑OR。
	 */
	@Test
	public void testAndOr() {
		Predicate<String> stringPredicate = s -> s.length() > 2;
		// 返回一个组合谓词，表示此谓词和另一个谓词的短路逻辑AND。 在评估组合谓词时，如果此谓词为false，则不评估其他谓词。
		boolean test1 =
				stringPredicate // 1.1先判断第一个谓词是否符合条件，如果符合了，则进行第二个判断
						.and(s -> s.startsWith("a"))
						.test("abcde"); // 1.2 进行第二个谓词判断
		System.out.println(test1);

		Predicate<String> stringPredicate1 = s -> s.startsWith("a");
		Predicate<String> stringPredicate2 = s -> s.endsWith("b");
		boolean acdldlgdb =
				stringPredicate.and(stringPredicate1).and(stringPredicate2).test("acdldlgdb");
		System.out.println(acdldlgdb);
		// 返回一个组合谓词，表示此谓词和另一个谓词的短路逻辑OR。 在评估组合谓词时，如果此谓词为true,则返回true。
		boolean bgh = stringPredicate.or(stringPredicate1).test("bgh");
		System.out.println(bgh);
	}

	/**
	 * Predicate<T> negate():返回表示此谓词的逻辑否定的谓词。
	 * {@link Predicate#negate()}
	 * {@link Predicate#isEqual(Object)} 返回一个谓词，根据Objects.equals（Object，Object）测试两个参数是否相等。
	 */
	@Test
	public void testNegate() {
		Predicate<String> stringPredicate = s -> s.length() > 2;
		boolean test = stringPredicate.negate().test("3057");
		System.out.println(test);

		Predicate<Object> sun = Predicate.isEqual("sun");
		boolean sun1 = sun.test("sun");
		System.out.println(sun1);
	}

	@Test
	public void testStudy(){
		Predicate<String> predicate = (s) -> s.length() > 0;
		System.out.println(predicate.test("lala"));
		Predicate<String> predicate1 = (s) -> s.startsWith("l");
		// and
		// 在评估组合谓词时，如果此谓词为假，则不评估另一个谓词。
		// 评估过程中评估中继给调用者的任何异常;如果对此谓词的评估抛出异常，则不会评估其他谓词。
		System.out.println(predicate1.and(predicate).test("ss"));
		// negate是逻辑否决谓词，取相反值
		System.out.println(predicate.negate().test("ss"));

		Predicate<Boolean> nonNull = Objects::nonNull;
		System.out.println("nonNull : " + nonNull.test(null));
		Predicate<Boolean> isNull = Objects::isNull;
		System.out.println("isNull : " + isNull.test(null));


		Predicate<String> isEmpty = String::isEmpty;
		System.out.println("isEmpty : " + isEmpty.test("lala"));
		Predicate<String> isEmptynegate = isEmpty.negate();
		System.out.println("isEmptyNegate : " + isEmptynegate.test("lala"));
	}
}
