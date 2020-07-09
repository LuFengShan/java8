package com.java8.concurrent;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * 同步并发的操作，主要是在一个线程中放置和获取元素
 */
public class BlockingQueueTest {
	public static void main(String[] args) {
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
			queue.offer(i);
//				queue.put(i);
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