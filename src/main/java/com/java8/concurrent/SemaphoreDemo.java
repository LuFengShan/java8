package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 计算信号量。虽然锁通常授予对变量或资源的独占访问权，但信号量能够维护整套许可。这在您必须限制对应用程序某些部分的并发访问量的不同情况下非常有用。
 */
public class SemaphoreDemo {

	@Test
	public void test() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 大小为5的信号量，从而将并发访问限制为5
		Semaphore semaphore = new Semaphore(5);

		Runnable longRunningTask = () -> {
			boolean permit = false;
			try {
				// 后续调用都会tryAcquire()经过一秒的最大等待超时
				permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("Semaphore acquired");
					ConcurrentUtils.sleep(5);
				} else {
					System.out.println("Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};

		// 执行程序可以同时运行10个任务
		IntStream.range(0, 10)
				.forEach(i -> executor.submit(longRunningTask));

		ConcurrentUtils.stop(executor);
	}
}
