package com.java8.pattern;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式 要么匹配字符，要么匹配位置
 * 正则就是要模糊匹配的
 *
 * @Author sgx
 * @Date 2019/5/22 10:06
 * @Version
 **/
public class PatternTest {
	private Logger log = LoggerFactory.getLogger(PatternTest.class);

	/**
	 * 两种模糊匹配
	 * <p>横向模糊匹配:横向模糊指的是，一个正则可匹配的字符串的长度不是固定的，可以是多种情况的。
	 * 其实现的方式是使用量词。譬如{m,n}，表示连续出现最少m次，最多n次。</p>
	 *
	 * <p>纵向模糊匹配</p>
	 */
	@Test
	public void test01() {
		String p = "/hello/";
		String m = "wohelloanja";
		matcherPattern(p, m);
		p = "/ab{2,5}c/g";
		m = "abc abbc abbbc abbbbc abbbbbc abbbbbbc";
		matcherPattern(p, m);

	}

	/**
	 * 匹配正则表达式，如果提供的字符串存在，则输出所在的位置
	 *
	 * @param p 正则表达式
	 * @param m 检验的字符串
	 */
	private void matcherPattern(String p, String m) {
		Pattern pattern = Pattern.compile(p);
		Matcher matcher = pattern.matcher(m);
		if (matcher.matches())
			log.info(() -> String.valueOf(true));
		log.info(() -> "over");
	}
}
