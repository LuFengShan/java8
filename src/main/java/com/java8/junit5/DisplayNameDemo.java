package com.java8.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link DisplayName#value()} 显示名称
 * <p>测试类和测试方法可以声明自定义的显示名称 – 空格、特殊字符甚至是emojis表情 – 都可以显示在测试运行器和测试报告中。
 */
@DisplayName("DisplayNameDemo")
class DisplayNameDemo {

	@Test
	@DisplayName("Custom test name containing spaces")
	void testWithDisplayNameContainingSpaces() {
	}

	@Test
	@DisplayName("╯°□°）╯")
	void testWithDisplayNameContainingSpecialCharacters() {
	}

	@Test
	@DisplayName("😱")
	void testWithDisplayNameContainingEmoji() {
	}

}
