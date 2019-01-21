package com.java8.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedLock {

	/**
	 * 锁定支持各种细粒度控制方法
	 */
	public void test01(){
		ExecutorService executor = Executors.newFixedThreadPool(2);
		ReentrantLock lock = new ReentrantLock();

		executor.submit(() -> {
			lock.lock();
			try {
				ConcurrentUtils.sleep(1);
			} finally {
				lock.unlock();
			}
		});

		executor.submit(() -> {
			System.out.println("Locked: " + lock.isLocked());
			System.out.println("Held by me: " + lock.isHeldByCurrentThread());
			// tryLock()作为替代lock()尝试获取锁而不暂停当前线程。必须使用布尔结果来检查在访问任何共享可变变量之前是否实际获取了锁。
			boolean locked = lock.tryLock();
			System.out.println("Lock acquired: " + locked);
		});

		ConcurrentUtils.stop(executor);

	}

	ReentrantLock lock = new ReentrantLock();
	int count = 0;

	/**
	 * 锁定通过lock()并通过发布获取unlock()。
	 * 将代码包装到try/finally块中以确保在异常情况下解锁非常重要。
	 * 这个方法就像同步的对应方式一样是线程安全的。如果另一个线程已经获取了锁，则后续调用将lock()暂停当前​线程，直到解锁为止。
	 * 在任何给定时间只有一个线程可以保持锁定。
	 */
	void increment() {
		lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
		}
	}
}
