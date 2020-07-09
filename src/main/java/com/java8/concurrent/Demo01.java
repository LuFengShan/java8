package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Demo01 {
	@Test
	public void test01() throws ExecutionException, InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(5);
		List<Future> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Future<Integer> future = service.submit(() -> ThreadLocalRandom.current().nextInt());
			list.add(future);
		}
		service.shutdown();
		
		for (Future f : list) {
			System.out.println("res:" + f.get());
		}
	}
	
	@Test
	public void test02() {
		Executors.newCachedThreadPool();
		ExecutorService service = Executors.newFixedThreadPool(10);
		while (true) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " is running ..");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
