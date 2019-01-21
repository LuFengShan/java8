package com.java8.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * synchronized所同步的方法和变量都是显式锁定，不支持隐式锁定，如果要使用隐式锁定要使用Lock
 */
public class Synchronized1 {
	public static void main(String[] args) {
		// new Synchronized1().testSynchroized01();
		new Synchronized1().testSynchroized02();
	}

	private void testSynchroized01() {
		ExecutorService executor = Executors.newFixedThreadPool(20);
		// 如果两个线程并行执行这些步骤，则两个线程可能同时执行步骤1，从而读取相同的当前值。这导致写入丢失，因此实际结果较低。
		IntStream.range(0, 50000).forEach(i -> executor.submit(this::increment));
		ConcurrentUtils.stop(executor);
		// 而不是看到10000的常量结果计数，实际结果随着上述代码的每次执行而变化。原因是我们在不同的线程上共享一个可变变量，而不同步对该变量的访问
		// 竞争条件：
		// （i）读取当前值
		// （ii）将该值增加1并且
		// （iii）将新值写入变量
		System.out.println("count : " + count); // 49965
		// 解决方案
		// 1.increment方法前面增加上synchronized关键字来修复竞争条件
	}

	/**
	 * 复用synchroized来修复竞争条件
	 */
	private void testSynchroized02() {
		ExecutorService executor = Executors.newFixedThreadPool(20);
		IntStream.range(0, 40000).forEach(i -> executor.submit(this::incrementSync));
		ConcurrentUtils.stop(executor);
		System.out.println("count : " + count); // 40000
	}

	

	int count = 0;

	/**
	 * 没有同步方法，在多线程的时候将导致结果出错
	 */
	void increment() {
		count = count + 1;
	}

	/**
	 * 方法的同步
	 */
	synchronized void incrementSync() {
		count = count + 1;
	}

	/**
	 * 方法中的同步代码块
	 */
	void incrementSync2() {
		synchronized (this) {
			count = count + 1;
		}

	}
}
