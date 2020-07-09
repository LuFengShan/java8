package com.java8.concurrent.my;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
	public static void main(String[] args) throws IOException {
		int corePoolSize = 2;
		int maximumPoolSize = 4;
		long keepAliveTime = 20L;
		TimeUnit unit = TimeUnit.SECONDS;
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
		
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				unit,
				workQueue,
				new MyThreadFactory("sgx"),
				new ThreadPoolExecutor.AbortPolicy()
		);
		
		executor.prestartAllCoreThreads();
		
		Runnable task = () -> {
			System.out.println(LocalDateTime.now() + UUID.randomUUID().toString());
		};
		
		for (int i = 0; i < 11; i++) {
			executor.execute(task);
		}
	}
}
