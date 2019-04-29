package com.java8.function;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Function;

/** Function功能接口: Function< T, R > -> 接收T对象，返回R对象 */
public class FunctionTest {

  /** R apply(T t):将此函数应用于给定的参数返回功能结果 */
  @Test
  public void functionTestApply() {
    // 1.功能性接口的实现。用例1：把Integer转换成String类型
    Function<String, Integer> stringConverterInteger = Integer::valueOf;
    // 2.用法
    Integer apply = stringConverterInteger.apply("123");
    System.out.println(apply);
  }

  /**
   * default <V> Function<T,V>:
   * 返回首先将此函数应用于其输入的组合函数，然后将after函数应用于结果。 如果对任一函数的求值抛出异常，则将其转发给组合函数的调用者。
   * 类型参数： V - 后函数和组合函数的输出类型
   * 参数： after - 应用此功能后应用的功能
   * 返回： 一个组合函数，首先应用此函数，然后应用after函数
   * 抛出： NullPointerException - 如果after为null
   */
  @Test
  public void functionTestAndThen() {
    // 1.定义一个组合函数
    Function<String, Integer> stringConverterInteger = Integer::valueOf;
    // 2
    Function<String, Object> integerConverterString =
        stringConverterInteger // 2.1 先把输入的参数应用于上面定义的stringConverterInteger函数,得到结果456
            .andThen(Objects::toString); // 2.2 然后在把结果(456)应用于after函数Objects::toString得到结果“456”
    // 3.处理元素
    Object apply = integerConverterString.apply("456");
    System.out.println(apply);
  }

  /**
   * default <V> Function<T,V>
   * 返回一个组合函数，该函数首先将before函数应用于其输入，然后将此函数应用于结果。 如果对任一函数的求值抛出异常，则将其转发给组合函数的调用者。
   * 类型参数： V - before函数和组合函数的输入类型
   * 参数： before - 应用此函数之前应用的函数
   * 返回： 一个组合函数，首先应用before函数，然后应用此函数
   * 抛出： NullPointerException - 如果before为null
   */
  @Test
  public void functionTestCompose() {
    // 1.定义一个组合函数
    Function<String, Integer> stringConverterInteger = Integer::valueOf;
    // 2.
    Function<String, Integer> compose = stringConverterInteger // 2.2：2.1处理完以后在用这个组合函数处理
            .compose((String str) -> str.substring(0, 2)); // 2.1：在组合函数调用之前先用这个before函数处理输入的元素
    // 3.处理元素
    Integer apply = compose.apply("282");
    System.out.println(apply);
  }

  /**
   * static <T> Function<T,T> identity()
   * 返回一个始终返回其输入参数的函数。
   * 类型参数： T  - 函数的输入和输出对象的类型
   * 返回： 一个总是返回其输入参数的函数
   */
  @Test
  public void functionTestByIdentity() {
    Function<String, String> function = s -> s + " -> !!";
    String apply = function.apply("hello word");
    System.out.println(apply);
  }
}
