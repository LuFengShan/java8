package com.java8.concurrent;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Java 从JDK 1.0开始支持Threads。在开始新线程之前，您必须指定此线程要执行的代码，通常称为任务task 。
 */
public class ThreadDemo {

	/**
	 * 这是通过实现Runnable- 一个定义单个void no-args方法的功能接口来完成的，run()
	 */
	@Test
	public void testRunnable() {
		// 1. lambad指定run的实现过程
		Runnable task = () -> {
			String name = Thread.currentThread() // 返回对当前正在执行的线程对象的引用。
					.getName(); // 返回此线程的名称。
			System.out.println("线程名字 ： " + name);
		};
		// 2. 调用实现1
		task.run();

		// 3. 首先，我们在开始一个新线程之前直接在主线程上执行runnable。
		Thread thread = new Thread(task);
		thread.start();

		System.out.println("OVER");
	}

	@Test
	public void testRunnable2() {
		Runnable task = () -> {
			try {
				System.out.println("Foo " + Thread.currentThread().getName());
				Thread.sleep(1000);
				System.out.println("Bar " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(task);
		thread.start();
		// Foo Thread-1
	}

	@Test
	public void testRunnable3() {
		Runnable task = () -> {
			try {
				System.out.println("Foo " + Thread.currentThread().getName());
				TimeUnit.SECONDS.sleep(1);
				System.out.println("Bar " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(task);
		thread.start();
		// Foo Thread-1
	}
}
