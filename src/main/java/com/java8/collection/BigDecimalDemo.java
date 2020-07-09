package com.java8.collection;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BigDecimalDemo {
	
	@Test
	public void testReduce() {
		
		Stream<Integer> intNumbers = Stream.of(5, 1, 100);
		int result = intNumbers.reduce(0, Integer::sum);
		assertEquals(106, result);
		
	}
	
	@Test
	public void testBigDecimalReduce() {
		
		Stream.Builder<BigDecimal> builder = Stream.<BigDecimal>builder();
		for (int i = 0; i < 10000; i++) {
			builder.accept(new BigDecimal("549953175"));
			builder.accept(new BigDecimal(2012545455.2546));
			builder.accept(new BigDecimal(52254654));
		}
		Stream<BigDecimal> stream = builder.build();
		BigDecimal reduce = stream.reduce(BigDecimal.ZERO, BigDecimal::add);
		System.out.println(reduce);
		
	}
}
