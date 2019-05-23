package com.java8.function;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OptionalTest {
	private Logger log = LoggerFactory.getLogger(OptionalTest.class);

	@Test
	public void optionalTest() {
		Optional<String> optional = Optional.of("bam");

		optional.isPresent(); // true
		optional.get(); // "bam"
		optional.orElse("fallback"); // "bam"

		optional.ifPresent((s) -> System.out.println(s.charAt(0))); // "b"

		Optional<Integer> optional2 = Optional.of(255);
		System.out.println(optional2.isPresent());
		System.out.println(optional2.orElse(200));
		System.out.println(optional2.get());

	}

	@Test
	public void optionalEmpty() {
		Optional<Person> empty = Optional.empty();
		System.out.println(empty.get()); // java.util.NoSuchElementException: No value present
	}

	/**
	 * {@link Optional#of(Object)} 和 {@link Optional#ofNullable(Object)}方法创建包含值的 Optional。
	 * 两个方法的不同之处在于如果你把 null 值作为参数传递进去，of() 方法会抛出 NullPointerException：
	 * <pre>
	 *  Optional<Person> person1 = Optional.of(nullPerson);
	 *  System.out.println(person1.get()); // java.lang.NullPointerException
	 * </pre>
	 * <p>
	 * 如果要检查Optional中的对象不是不null，有{@link Optional#isPresent()}和{@link Optional#ifPresent(Consumer)},这两个
	 * 方法的不同之处是<b>isPresent</b>只是检查是不是null,
	 * <b>ifPresent</b>除了执行检查，还接受一个Consumer(消费者) 参数，如果对象不是空的，就对执行传入的 Lambda 表达式
	 * </p>
	 */
	@Test
	public void optionalOf() {
		Person nullPerson = null;
		Person lisa = new Person("sa", "li");

		// 明确对象不为 null  的时候使用 of()
		Optional<Person> lisaPerson = Optional.of(lisa);
		System.out.println(lisaPerson.get());
		// 如果对象即可能是 null 也可能是非 null，你就应该使用 ofNullable() 方法：
		Optional<Person> nullPerson1 = Optional.ofNullable(nullPerson);
		// System.out.println(nullPerson1.get()); // 抛出异常
		// 所以要避免异常，首先要检查,使用isPresent
		System.out.println(nullPerson1.isPresent());
		// 检查是否有值的另一个选择是 ifPresent() 方法。该方法除了执行检查，
		// 还接受一个Consumer(消费者) 参数，如果对象不是空的，就对执行传入的 Lambda 表达式：
		nullPerson1.ifPresent(u -> System.out.println(u.toString())); // 对象为空了，所以不执行lambad
		lisaPerson.ifPresent(u -> System.out.println(u.toString())); // 对象不为空了，执行lambad

	}

	/**
	 * 返回默认值
	 * Optional 类提供了 API 用以返回对象值，或者在对象为空的时候返回默认值
	 * {@link Optional#orElse(Object)} : 如果有值则直接返回，否则返回传递给他的参数值
	 * {@link Optional#orElseGet(Supplier)} : 其行为和<b>orElse</b>略有不同。这个方法会在有值的时候返回值，如果没有值，它会执行作为参数传入的 Supplier(供应者) 函数式接口，并将返回其执行结果
	 * <p><b>orElse和orElseGet的区别：</b>当对象为空而返回默认对象时，行为并无差异。如果传入的对象不为null时，
	 *
	 * </p>
	 */
	@Test
	public void optionalOrElse() {
		Person nullPerson = null;
		Person lisa = new Person("sa", "li");
		// 如果nullPerson为null，则返回lisa
		Person person = Optional.ofNullable(nullPerson).orElse(lisa);
		System.out.println(person);

		// 对象的初始值不是null，那么默认值lisa会被忽略
		Person wangWu = new Person("wu", "wang");
		Person person1 = Optional.ofNullable(wangWu).orElse(lisa);
		System.out.println(person1);
		// orElse和orElseGet的区别
		// 1 传入的对象为null
		log.info(() -> "orElse和orElseGet的区别: 传入的对象为null");
		Person person2 = Optional.ofNullable(nullPerson)
				.orElse(createPerson());
		Person person3 = Optional.ofNullable(nullPerson)
				.orElseGet(() -> createPerson());
		// 2 传入的对象不为null
		// 这个示例中，两个 Optional  对象都包含非空值，两个方法都会返回对应的非空值。不过，
		// orElse() 方法仍然创建了 User 对象。与之相反，orElseGet() 方法不创建 User 对象。
		// 在执行较密集的调用时，比如调用 Web 服务或数据查询，这个差异会对性能产生重大影响。
		log.info(() -> "orElse和orElseGet的区别: 传入的对象不为null");
		Person person4 = Optional.ofNullable(lisa)
				.orElse(createPerson());
		Person person5 = Optional.ofNullable(lisa)
				.orElseGet(() -> createPerson());
	}

	/**
	 * 返回异常
	 * Optional 还定义了 orElseThrow() API —— 它会在对象为空的时候抛出异常，而不是返回备选的值
	 * 这个orElseThrow可以指定我们自己想要的异常信息。
	 */
	@Test
	public void optionalOrElseThrow() throws MyException {
		Person nullPerson = null;
		Person person = Optional.ofNullable(nullPerson)
				.orElseThrow(MyException::new);
		System.out.println(person);
	}

	/**
	 * 我们从 map() 和 flatMap() 转换 Optional的值
	 */
	@Test
	public void optionalMap() {
		Person lisa = new Person("sa", "li");
		String name = Optional.ofNullable(lisa)
				.map(Person::getFirstName)
				.orElse("default first name");
		log.error(() -> name);

		// flatMap
		lisa.setPosition("20190429");
		String s = Optional.ofNullable(lisa)
				.flatMap(Person::getPosition)
				.orElse("9999999");
		log.error(() -> s);
	}

	/**
	 * Optional的链式过滤
	 */
	@Test
	public void optionalFilter() {
		Person lisa = new Person("sa", "li");
		Optional.ofNullable(lisa)
				.filter(person -> Objects.equals("sa", person.getFirstName())) // 判断firstname是不是以‘s’开始
				.ifPresent(person -> log.info(() -> person.toString())); // 如果是就输出这个person的属性，不是，则什么也不输出

		String li = Optional.ofNullable(lisa)
				.filter(person -> Objects.equals("li", person.getLastName()))
				.orElse(createPerson())
				.toString();
		log.error(() -> li);

	}

	private Person createPerson() {
		log.warn(() -> "createPerson");
		return new Person("jiang", "song");
	}

}

/**
 * 自定义异常
 */
class MyException extends Exception {
	private Logger log = LoggerFactory.getLogger(MyException.class);

	public MyException() {
		log.info(() -> "MyException");
	}
}
