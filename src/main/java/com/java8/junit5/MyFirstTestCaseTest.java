package com.java8.junit5;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author sgx
 */
@DisplayName("我的第一个测试用例")
public class MyFirstTestCaseTest {

	@BeforeAll
	public static void init() {
		System.out.println("初始化数据");
	}

	@AfterAll
	public static void cleanup() {
		System.out.println("清理数据");
	}

	@BeforeEach
	public void tearup() {
		System.out.println("当前测试方法开始");
	}

	@AfterEach
	public void tearDown() {
		System.out.println("当前测试方法结束");
	}

	@DisplayName("我的第一个测试")
	@Test
	void testFirstTest() {
		System.out.println("我的第一个测试开始测试");
	}

	@DisplayName("我的第二个测试")
	@Test
	void testSecondTest() {
		System.out.println("我的第二个测试开始测试");
	}

	@DisplayName("我的第三个测试")
	@Test
	@Disabled
	void testTrirdTest(){
		System.out.println("我的第三个测试开始测试");
	}

	/**
	 * @RepeatedTest 注解内用
	 * currentRepetition 变量表示已经重复的次数，
	 * totalRepetitions 变量表示总共要重复的次数，
	 * displayName 变量表示测试方法显示名称，
	 * 我们直接就可以使用这些内置的变量来重新定义测试方法重复运行时的名称
	 */
	@DisplayName("重复测试")
	@RepeatedTest(value = 5, name = "{displayName}， 总次数 {totalRepetitions}，目前是 第 {currentRepetition} 次")
	void i_am_a_repeated_test() {
		testSecondTest();
	}

	/**
	 * 新的断言操作
	 */
	@Test
	void testGroupAssertions() {
		int[] numbers = {0, 1, 2, 3, 4};
		Assertions.assertAll("numbers",
				() -> Assertions.assertEquals(numbers[1], 1),
				() -> Assertions.assertEquals(numbers[3], 3),
				() -> Assertions.assertEquals(numbers[4], 4)
		);
	}

	@Test
	@DisplayName("超时方法测试")
	void test_should_complete_in_one_second() {
		Assertions.assertTimeoutPreemptively(Duration.of(1, ChronoUnit.SECONDS), () -> Thread.sleep(2000));
	}

	@Test
	@DisplayName("测试捕获的异常")
	void assertThrowsException() {
		String str = null;
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Integer.valueOf(str);
		});
	}
}
