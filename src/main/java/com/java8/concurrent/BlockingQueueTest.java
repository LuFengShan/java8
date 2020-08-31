package com.java8.concurrent;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 同步并发的操作，主要是在一个线程中放置和获取元素
 */
public class BlockingQueueTest {
	@Test
	public void test01() {
		// 设置一次只能放置10个对象的线程
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

//		new Thread(consumer).start();
//		new Thread(produce).start();
		ExecutorService service = Executors.newCachedThreadPool();
		service.submit(new Consumer(queue));
		for (int i = 0; i < 10; i++) {
			service.submit(new Produce(queue));
		}
		ConcurrentUtils.stop(service);
	}


	/**
	 * 同步并发的操作，主要是在一个线程中放置和获取元素
	 */

	@Test
	public void test02() {
		// 设置一次只能放置10个对象的线程
		BlockingQueue<Integer> queue = new LinkedBlockingQueue();
		ExecutorService service = Executors.newCachedThreadPool();
		Semaphore semaphore = new Semaphore(2);
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ProduceCallable(queue));
		service.submit(new ProduceCallable(queue));
//		ConcurrentUtils.stop(service);
	}

	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new LinkedBlockingQueue();
		ExecutorService service = Executors.newCachedThreadPool();
		Semaphore semaphore = new Semaphore(4);
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ConsumerCallable(queue, semaphore));
		service.submit(new ProduceCallable(queue));
		service.submit(new ProduceCallable(queue));
		service.submit(new ProduceCallable(queue));
		service.submit(new ProduceCallable(queue));
	}

	/**
	 * 判断当前时间是不是拨打电话
	 *
	 * @return 如果在可拨打时段返回true, 否则返回false
	 */
	public static boolean pritimePeriod() {
		// 9点
		LocalTime time9 = LocalTime.of(9, 0);
		// 12点
		LocalTime time12 = LocalTime.of(12, 0);
		// 13点
		LocalTime time13 = LocalTime.of(13, 0);
		// 18点
		LocalTime time18 = LocalTime.of(18, 0);
		// 当时时间
		LocalTime currentTime = LocalTime.now();
		// 如果当时时间在9-12点
		if (currentTime.isAfter(time9) && currentTime.isBefore(time12)) {
			return true;
		}
		if (currentTime.isAfter(time13) && currentTime.isBefore(time18)) {
			return true;
		}
		return false;
	}

}

/**
 * 一个线程生产对象
 */
class Produce implements Runnable {
	BlockingQueue<Integer> queue;

	public Produce(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@SneakyThrows
	@Override
	public void run() {

		while (true) {

			int i = ThreadLocalRandom.current().nextInt(100);
			System.out.println("生产一个对象 ： " + i);
//			queue.offer(i);
			queue.put(i);
		}

	}
}

/**
 * 一个线程消费对象
 */
class Consumer implements Runnable {
	BlockingQueue<Integer> queue;

	public Consumer(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@SneakyThrows
	@Override
	public void run() {
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(1L);
				System.out.println("消费一个对象 ： " + LocalDateTime.now() + ":" + queue.take().intValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ProduceCallable implements Runnable {
	BlockingQueue<Integer> queue;

	public ProduceCallable(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		IntStream.rangeClosed(1, 10).forEach(integer -> {
			try {
				queue.put(ThreadLocalRandom.current().nextInt(100000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}

class ConsumerCallable implements Runnable {
	BlockingQueue<Integer> queue;
	Semaphore semaphore;

	public ConsumerCallable(BlockingQueue<Integer> queue, Semaphore semaphore) {
		this.queue = queue;
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		while (true) {
			if (!BlockingQueueTest.pritimePeriod()) {
				System.out.println("不在使用时段内，直接结束当前线程:" + Thread.currentThread().getName());
				break;
			}
			try {
				// 后续调用都会tryAcquire()经过一秒的最大等待超时
				semaphore.acquire();
				System.out.println("取得凭证:" + Thread.currentThread().getName() + "---等待获取的线程数的估计:" + semaphore.getQueueLength());
				int i = queue.take().intValue();
				if (i < 20) {
					queue.put(i * 100000);
				} else {
					System.out.println("消费一个对象 ： " + i);
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				semaphore.release();
				System.out.println("释放凭证:" + Thread.currentThread().getName() + "---等待获取的线程数的估计:" + semaphore.getQueueLength());
			}
		}
	}
}