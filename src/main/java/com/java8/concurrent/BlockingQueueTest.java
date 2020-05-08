package com.java8.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.SplittableRandom;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 同步并发的操作，主要是在一个线程中放置和获取元素
 */
public class BlockingQueueTest {
	public static void main(String[] args) {
		// 设置一上只能放置10个对象的线程
		BlockingQueue<Dog> queue = new ArrayBlockingQueue<>(5);
		Produce produce = new Produce(queue);
		Consumer consumer = new Consumer(queue);
		new Thread(consumer).start();
		new Thread(produce).start();
	}
}

/**
 * 一个线程生产对象
 */
class Produce implements Runnable {
	BlockingQueue<Dog> queue = null;

	public Produce(BlockingQueue<Dog> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {

		for (int i = 0; i < 50; i++) {
			Dog dog = Dog.builder()
					.name(UUID.randomUUID().toString())
					.age(new SplittableRandom().nextInt(60))
					.build();
			System.out.println("生产一个对象 ： " + dog.toString());
			try {
				queue.put(dog);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

/**
 * 一个线程消费对象
 */
class Consumer implements Runnable {
	BlockingQueue<Dog> queue = null;

	public Consumer(BlockingQueue<Dog> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("消费一个对象 ： " + queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
class Dog {
	private String name;
	private int age;
}