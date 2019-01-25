package com.java8.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock接口指定另一种类型的锁，它保持一对锁以进行读写访问。
 * 读写锁定背后的意思是，只要没有人写入此变量，通常可以安全地同时读取可变变量。
 * 因此，只要没有线程保持写锁定，读锁就可以由多个线程同时保持。
 * 在读取比写入更频繁的情况下，这可以提高性能和吞吐量
 */
public class ReadWriteLockDemo {
	public static void main(String[] args) {
		// 1. 线程服务执行器
		ExecutorService service = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		// 2. 读写锁（这是一对锁）
		ReadWriteLock lock = new ReentrantReadWriteLock();
		// 3.1任务(往map中写入数据)
		Runnable writeTask = () -> {
			// 启动写锁定
			lock.writeLock().lock();
			// 这里放入try块中的想法是，无论是否出现异常都能保证这个锁释放
			try {
				// 线程睡1s
				ConcurrentUtils.sleep(2);
				map.put("gg", "争气的人");
			} finally {
				lock.writeLock().unlock();
			}
			// 释放写锁定
		};
		// 3.2执行器开始让写线程服务，首先获取写锁定，以便在睡眠2s后将新值放入
		service.submit(writeTask);

		// 4.1在写任务没有完成之前，开始两个读任务
		/**
		 * 两个读取任务都必须等待2s，直到写入任务完成。
		 * 释放写锁定后，两个读取任务并行执行，并将结果同时打印到控制台。
		 * 它们不必等待彼此完成，因为只要没有其他线程持有写锁定，就可以安全地同时获取读锁。
		 */
		Runnable readTask = () -> {
			lock.readLock().lock();
			try {
				System.out.println(map.get("gg") + ":" + System.nanoTime());
				ConcurrentUtils.sleep(1);
			} finally {
				lock.readLock().unlock();
			}

		};
		service.submit(readTask);
		service.submit(readTask);

		ConcurrentUtils.stop(service);
	}
}
