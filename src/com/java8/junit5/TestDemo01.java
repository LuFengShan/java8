package com.java8.junit5;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDemo01 {
	private static Logger log = Logger.getLogger("TestDemo01");

	@Test
	public void testMain() {
		log.info("testMain()");
	}

	// @DisplayName - 定义测试类或测试方法的自定义显示名称
	@DisplayName("Single test successful")
	@Test
	void testSingleSuccessTest() {
		log.info("Success");
	}

	@Test
	// @Disable - 用于禁用测试类或方法（以前为@Ignore）
	@Disabled("Not implemented yet")
	void testShowSomething() {
	}

	// 异常测试第一种：用于验证抛出异常的更多细节
	@Test
	void shouldThrowException() {
		Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
			throw new UnsupportedOperationException("Not supported");
		});
		assertEquals(exception.getMessage(), "Not supported");
	}

	// 异常测试第二种：仅验证异常类型。
	@Test
	void assertThrowsException() {
		String str = null;
		assertThrows(IllegalArgumentException.class, () -> {
			Integer.valueOf(str);
		});
	}


	@BeforeAll
	static void setup() {
		log.info("@BeforeAll - 表示将在当前类中的所有测试方法之前执行带注释的方法（以前为@BeforeClass）");
	}

	@BeforeEach
	void init() {
		log.info("@BeforeEach - 表示在每个测试方法之前执行带注释的方法（之前为@Before）");
	}

	@AfterAll
	static void afterAll() {
		log.info("@AfterAll - 表示将在当前类中的所有测试方法之后执行带注释的方法（以前为@AfterClass）");
	}

	@AfterEach
	void afterEach() {
		log.info("@AfterEach - 表示在每个测试方法之后将执行带注释的方法（之前为@After）");
	}


}
