package com.java8.suanfa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 排序算法
 */
public class SortDemo {

	private Logger log = LoggerFactory.getLogger(SortDemo.class);
	private static int[] arr = new int[80000];

	static {
		for (int i = 0; i < 80000; i++) {
			arr[i] = (int) (Math.random() * 80000);
		}
	}

	@BeforeEach
	public void startTime() {
		log.info(() -> LocalDateTime.now().toString());
	}

	@AfterEach
	public void endTime() {
		log.info(() -> LocalDateTime.now().toString());
	}

	@Test
	public void bubbleSort() {
		System.out.println("aa");

	}
}
