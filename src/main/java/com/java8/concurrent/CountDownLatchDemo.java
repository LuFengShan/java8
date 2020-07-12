package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {
	@Test
	public void test() {
		CountDownLatch latch = new CountDownLatch(2);
		Runnable task1 = () -> {
			System.out.println("task1" + Thread.currentThread().getName() + "running...");
			try {
				TimeUnit.SECONDS.sleep(5L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("task1" + Thread.currentThread().getName() + "over");
			latch.countDown();
		};
		Runnable task2 = () -> {
			System.out.println("task2" + Thread.currentThread().getName() + "running...");
			try {
				TimeUnit.SECONDS.sleep(3L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("task2" + Thread.currentThread().getName() + "over");
			latch.countDown();
		};
		ExecutorService service = Executors.newFixedThreadPool(2);
		service.submit(task1);
		service.submit(task2);
		ConcurrentUtils.stop(service);
		System.out.println("thread 2 over");
		try {
			latch.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("go main");
	}
}
