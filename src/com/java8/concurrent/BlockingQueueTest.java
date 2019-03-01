package com.java8.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {
	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
		Produce produce = new Produce(queue);
		Consumer consumer = new Consumer(queue);
		new Thread(consumer).start();
		new Thread(produce).start();
	}
}

class Produce implements Runnable {
	BlockingQueue<Integer> queue = null;

	public Produce(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {

		for (int i = 0; i < 50; i++) {
			System.out.println("生产一个对象 ： " + i);
			try {
				queue.put(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

class Consumer implements Runnable {
	BlockingQueue<Integer> queue = null;

	public Consumer(BlockingQueue<Integer> queue) {
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