package com.java8.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 执行器工具类，主要是测试类有过多的关闭等操作
 */
public class ConcurrentUtils {

	/**
	 * 执行器关闭
	 * @param executor
	 */
	public static void stop(ExecutorService executor) {
		try {
			executor.shutdown();
			executor.awaitTermination(60, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			System.err.println("termination interrupted");
		}
		finally {
			if (!executor.isTerminated()) {
				System.err.println("killing non-finished tasks");
			}
			executor.shutdownNow();
		}
	}

	/**
	 * 线程睡一定时间
	 * @param seconds
	 */
	public static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
