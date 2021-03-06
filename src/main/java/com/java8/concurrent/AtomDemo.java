package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.stream.IntStream;

/**
 * 在内部，原子类大量使用比较和交换（CAS）
 * 在开发中优先选择原子类而不是锁
 */
public class AtomDemo {
	
	@Test
	public void testAtomicIntegerDemo() {
		AtomicInteger atomicInteger = new AtomicInteger(0);
		
		// 原子增加一个当前值,也就是增加1，返回的是旧值
		System.out.println(atomicInteger.getAndIncrement());
		// 得到原子的当前的值
		System.out.println(atomicInteger.get());
		// 原子增加一个当前值，也就是1，返回的是增加以后的值
		System.out.println(atomicInteger.incrementAndGet());
		// 把原子的值设置为给定的新值
		atomicInteger.set(10000);
		System.out.println(atomicInteger.get());
		// 把原子的值设置为给定的新值，并返回旧值
		System.out.println(atomicInteger.getAndSet(5000));
		System.out.println(atomicInteger.get());
		// 如果当前的值等于预期的值，则将该原子的值设置为指定的更新的值
		boolean b = atomicInteger.compareAndSet(5000, 6000);
		System.out.println(b);
		System.out.println(atomicInteger.get());
		// 在原子上减少一个当前值，也就是减少1，返回的是旧值
		System.out.println(atomicInteger.getAndDecrement());
		System.out.println(atomicInteger.get());
		// 在原子上减少一个当前值，也就是减少1，返回的是新值
		System.out.println(atomicInteger.decrementAndGet());
		// 将给定的值原子的增加当前值，返回的是旧值
		// 增加800
		System.out.println(atomicInteger.getAndAdd(800));
		System.out.println(atomicInteger.get());
		// 将给定的值原子的增加当前值，返回的是新值
		System.out.println(atomicInteger.addAndGet(800));
		// 不敢原子以前的值是多少，最终把值设定为100
		atomicInteger.lazySet(100);
		System.out.println(atomicInteger.get());
	}
	
	/**
	 * 通过使用AtomicInteger作为替代，Integer我们能够在线程安全的庄园中同时增加数量，而无需同步对变量的访问。
	 * 该方法incrementAndGet()是一个原子操作，因此我们可以安全地从多个线程调用此方法。
	 */
	@Test
	public void testAtomicInteger() {
		AtomicInteger atomicInt = new AtomicInteger(0);
		// 创建下个核心线程数量（2）和最大线程数量一样的无界的线程池
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		IntStream.range(0, 1000)
				.forEach(i -> executor.submit(atomicInt::incrementAndGet));
		
		ConcurrentUtils.stop(executor);
		// => 1000
		System.out.println(atomicInt.get());
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
		
		// => 2000
		System.out.println(atomicInt.get());
	}
	
	@Test
	public void testAtomicStampedReference() {
		String expectedReference = "abc";
		String newReference = "def";
		AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>(expectedReference, 1);
		//1、 如果当前引用 == 预期引用，则以原子方式将该标志的值设置为给定的更新值。
		Boolean bool = atomicStampedReference.attemptStamp("abc", 5);
		System.out.println("Boolean: " + bool);
		System.out.println("atomicStampedReference : reference  " + atomicStampedReference.getReference());
		System.out.println("atomicStampedReference : stampe  " + atomicStampedReference.getStamp());
		
		
		// 如果当前引用 == 预期引用，并且当前标志等于预期标志，则以原子方式将该引用和该标志的值设置为给定的更新值。
		bool = atomicStampedReference.compareAndSet("abc", "def", 5, 10);
		System.out.println("Boolean: " + bool);
		System.out.println("atomicStampedReference : reference  " + atomicStampedReference.getReference());
		System.out.println("atomicStampedReference : stampe  " + atomicStampedReference.getStamp());
		
		
		
		
	}
}
