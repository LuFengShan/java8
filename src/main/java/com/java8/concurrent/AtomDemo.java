package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 在内部，原子类大量使用比较和交换（CAS）
 * 在开发中优先选择原子类而不是锁
 */
public class AtomDemo {

	/**
	 * 通过使用AtomicInteger作为替代，Integer我们能够在线程安全的庄园中同时增加数量，而无需同步对变量的访问。
	 * 该方法incrementAndGet()是一个原子操作，因此我们可以安全地从多个线程调用此方法。
	 */
	@Test
	public void testAtomicInteger() {
		AtomicInteger atomicInt = new AtomicInteger(0);
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
				.forEach(i -> executor.submit(atomicInt::incrementAndGet));

		ConcurrentUtils.stop(executor);
		System.out.println(atomicInt.get());    // => 1000
	}

	/**
	 * AtomicInteger支持各种原子操作。该方法updateAndGet()接受lambda表达式，以便对整数执行任意算术运算
	 */
	@Test
	public void testAtomicInteger02() {
		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
				.forEach(i -> {
					Runnable task = () ->
							atomicInt.updateAndGet(n -> n + 2);
					executor.submit(task);
				});

		ConcurrentUtils.stop(executor);

		System.out.println(atomicInt.get());    // => 2000
	}
}
