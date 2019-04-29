package com.java8.function;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Consumer Consumer< T > 接收T对象，不返回值
 *
 * <ul>
 *   <li>消费某个对象的接口
 *   <li>1 作用 消费某个对象
 *   <li>2 使用场景 Iterable接口的forEach方法需要传入Consumer，大部分集合类都实现了该接口，用于返回Iterator对象进行迭代。
 * </ul>
 */
public class ConsumerTest {

  /** void accept(T t) 对给定的参数执行此操作。 */
  @Test
  public void testAccept() {
    // 1. 给指定字符串增加“!!”
    Consumer<String> consumer = s -> String.format("%s!!", s);
    consumer.accept("sgx");

    Consumer<Person> personConsumer =
        person -> {
          if (person.firstName.startsWith("l")) {
            System.out.println(person.toString());
          }
        };
    List<Person> people =
        Arrays.asList(
            new Person("la", "la"),
            new Person("al", "al"),
            new Person("bl", "bl"),
            new Person("lc", "lc"));
    people.forEach(personConsumer);
  }

	@Test
  public void testAndThen() {
    // 1. 给指定字符串增加“!!”
    Consumer<String> consumer = s -> String.format("%s -> ", s);
    consumer.accept("ahah");
    // 2. 第一次消费后对元素结果进行的后置的函数
    Consumer<String> andThen = consumer.andThen(s -> s.substring(0, 2));
    System.out.println(andThen.toString());
  }
}
