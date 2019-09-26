package com.java8.junit5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 内嵌测试类：@Nested
 * 当我们编写的类和代码逐渐增多，随之而来的需要测试的对应测试类也会越来越多。
 * 为了解决测试类数量爆炸的问题，JUnit 5提供了@Nested 注解，能够以静态内部成员类的形式对测试用例类进行逻辑分组。
 * 并且每个静态内部类都可以有自己的生命周期方法， 这些方法将按从外到内层次顺序执行。
 * 此外，嵌套的类也可以用@DisplayName 标记，这样我们就可以使用正确的测试名称
 *
 * @author sgx
 */
@DisplayName("内嵌测试类")
public class NestUnitTest {
	@BeforeEach
	void init() {
		System.out.println("测试方法执行前准备");
	}

	@Nested
	@DisplayName("第一个内嵌测试类")
	class FirstNestTest {
		@Test
		void test() {
			System.out.println("第一个内嵌测试类执行测试");
		}
	}

	@Nested
	@DisplayName("第二个内嵌测试类")
	class SecondNestTest {
		@Test
		void test() {
			System.out.println("第二个内嵌测试类执行测试");
		}
	}
}
