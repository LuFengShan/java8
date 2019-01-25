package com.java8.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock，它也支持读写锁，就像ReadWriteLock一样。
 * StampedLock在锁定的时候会返回这个锁的标记。
 * 您可以使用这些标记释放锁定或检查锁定是否仍然有效。
 * 另外，标记锁支持另一种称为乐观锁定的锁定模式
 */
public class StampedLockDemo {
	public static void main(String[] args) {
		// test01();
		// test02();
		test03();
	}

	public static void test01() {
		ExecutorService service = Executors.newFixedThreadPool(2);
		Map<String, Integer> map = new HashMap<>();
		StampedLock lock = new StampedLock();
		// 写线程
		Runnable writeLock = () -> {
			// 写锁锁定，并返回一个锁的标记writeLockMark
			long writeLockMark = lock.writeLock();
			try {
				ConcurrentUtils.sleep(2);
				map.put("aa", 10000);
			} finally {
				// 释放标记为writeLockMark的锁
				lock.unlockWrite(writeLockMark);
			}
		};
		// 启动写线程
		service.submit(writeLock);

		/**
		 * 通过readLock()或writeLock()返回一个标记来获取读取或写入锁定，
		 * 该标记稍后用于在finally块中解锁.
		 * 这个标记每次在释放后就失效了，每次调用都会返回一个新的标记
		 */
		Runnable readTask = () -> {
			long stamp = lock.readLock();
			try {
				System.out.println(map.get("aa"));
				ConcurrentUtils.sleep(1);
			} finally {
				lock.unlockRead(stamp);
			}
		};

		service.submit(readTask);
		service.submit(readTask);

		ConcurrentUtils.stop(service);
	}

	/**
	 * StampedLock的乐观锁定
	 */
	public static void test02() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, Integer> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				/**
				 * 调用获取乐观读锁定tryOptimisticRead()，无论锁定是否实际可用，它都会在不阻塞当前线程的情况下返回戳记
				 * 如果已经有写锁定激活，则返回的戳记等于零
				 */
				System.out.println("乐观锁有效？ : " + lock.validate(stamp));
				System.out.println(map.get("bb"));
				ConcurrentUtils.sleep(1);
				System.out.println("乐观锁有效？ : " + lock.validate(stamp));
				System.out.println(map.get("bb"));
				ConcurrentUtils.sleep(2);
				System.out.println("乐观锁有效？ : " + lock.validate(stamp));
				System.out.println(map.get("bb"));
			} finally {
				lock.unlock(stamp);
			}
		});

		/**
		 * 在将第一个线程发送到休眠状态一秒钟之后，第二个线程获得写锁定而不等待释放乐观读取锁定。
		 * 从这一点开始，乐观读锁定不再有效。即使释放写锁定，乐观读锁也会保持无效。
		 * 因此，在使用乐观锁时，每次访问任何共享的可变变量后都必须验证锁，以确保读取仍然有效。
		 */
		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println("获取 writeLock");
				map.put("bb", 2000);
				ConcurrentUtils.sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("写完毕");
			}
		});

		ConcurrentUtils.stop(executor);
	}

	private static int count = 0;
	/**
	 * 将读锁定转换为写锁定而不再进行解锁和锁定是可以的。StampedLock提供了tryConvertToWriteLock()用于此目的的方法
	 */
	public static void test03() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		/**
		 * 该任务首先获得读锁定并将当前的字段值打印count到控制台。
		 * 但是如果当前值为零，我们想要分配一个新值23。
		 * 我们首先必须将读锁转换为写锁，以免破坏其他线程的潜在并发访问。
		 * 调用tryConvertToWriteLock()不会阻塞，但可能会返回一个零标记，表示当前没有写锁定。
		 * 在这种情况下，我们调用writeLock()阻塞当前线程，直到写锁定可用。
		 */
		executor.submit(() -> {
			long stamp = lock.readLock();
			try {
				if (count == 0) {
					stamp = lock.tryConvertToWriteLock(stamp);
					if (stamp == 0L) {
						System.out.println("Could not convert to write lock");
						stamp = lock.writeLock();
					}
					count = 23;
				}
				System.out.println(count);
			} finally {
				lock.unlock(stamp);
			}
		});

		ConcurrentUtils.stop(executor);
	}
}
