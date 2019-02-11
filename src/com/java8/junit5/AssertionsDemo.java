package com.java8.junit5;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * JUnit Jupiter附带了很多JUnit 4就已经存在的断言方法，并增加了一些适合与Java8 Lambda一起使用的断言。
 * 所有的JUnit Jupiter断言都是 org.junit.jupiter.api.Assertions 类中static方法。
 */
class AssertionsDemo {

	@Test
	void standardAssertions() {
		assertEquals(2, 2);
		assertEquals(4, 4, "可选的断言消息现在是最后一个参数。");
		assertTrue(2 == 2, "可以懒惰地评估断言消息 - 以避免不必要地构造复杂消息。");
	}

//	@Test
//	void groupedAssertions() {
//		// In a grouped assertion all assertions are executed, and any
//		// failures will be reported together.
//		assertAll("person",
//				() -> assertEquals("John", person.getFirstName()),
//				() -> assertEquals("Doe", person.getLastName())
//		);
//	}
//
//	@Test
//	void dependentAssertions() {
//		// Within a code block, if an assertion fails the
//		// subsequent code in the same block will be skipped.
//		assertAll("properties",
//				() -> {
//					String firstName = person.getFirstName();
//					assertNotNull(firstName);
//
//					// Executed only if the previous assertion is valid.
//					assertAll("first name",
//							() -> assertTrue(firstName.startsWith("J")),
//							() -> assertTrue(firstName.endsWith("n"))
//					);
//				},
//				() -> {
//					// Grouped assertion, so processed independently
//					// of results of first name assertions.
//					String lastName = person.getLastName();
//					assertNotNull(lastName);
//
//					// Executed only if the previous assertion is valid.
//					assertAll("last name",
//							() -> assertTrue(lastName.startsWith("D")),
//							() -> assertTrue(lastName.endsWith("e"))
//					);
//				}
//		);
//	}

	@Test
	void exceptionTesting() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
			throw new IllegalArgumentException("a message");
		});
		assertEquals("a message", exception.getMessage());
	}

	@Test
	void timeoutNotExceeded() {
		// 以下断言成功。
		assertTimeout(ofMinutes(2), () -> {
			// 执行不到2分钟的任务。
		});
	}

	@Test
	void timeoutNotExceededWithResult() {
		// 以下断言成功，并返回提供的对象。
		String actualResult = assertTimeout(ofMinutes(2), () -> "a result");
		assertEquals("a result", actualResult);
	}

	@Test
	void timeoutNotExceededWithMethod() {
		// 以下断言调用方法引用并返回一个对象。
		String actualGreeting = assertTimeout(ofMinutes(2), AssertionsDemo::greeting);
		assertEquals("hello world!", actualGreeting);
	}

	@Test
	void timeoutExceeded() {
		// 以下断言失败，并显示类似于以下内容的错误消息：执行超出超时10毫秒到91毫秒
		assertTimeout(ofMillis(10), () -> {
			// 模拟超过10毫秒的任务。
			Thread.sleep(100);
		});
	}

	@Test
	void timeoutExceededWithPreemptiveTermination() {
		// 以下断言失败，并显示类似于以下内容的错误消息：10 ms后执行超时
		assertTimeoutPreemptively(ofMillis(10), () -> {
			// 模拟超过10毫秒的任务。
			Thread.sleep(100);
		});
	}

	private static String greeting() {
		return "hello world!";
	}

}
