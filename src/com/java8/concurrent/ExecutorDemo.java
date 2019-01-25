package com.java8.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorDemo {

	public static void main(String[] args) {
		test1();
	}

	public static void test1() {
		// 1. 该类Executors提供了方便的工厂方法来创建不同类型的执行程序服务。在此示例中，我们使用具有大小为1的线程池的执行程序
		ExecutorService executorService = Executors.newCachedThreadPool();
		Runnable task = () -> {
			try{
				TimeUnit.SECONDS.sleep(3);
			}catch (Exception e) {
				System.out.println("e : 任务中断");
			}
			String name = Thread.currentThread().getName();
			System.out.println("任务结果，线程名字 : " + name);
		};
		// 2. 执行线程
		executorService.submit(task);
		// 但是在运行代码时（我这个测试类除外）你会发现一个重要的区别：java进程永远不会停止！必须明确停止执行程序 - 否则他们会继续侦听新任务。
		// 3. 明确的停止
		//  ExecutorService为此提供了两种方法：shutdown()等待当前正在运行的任务完成，
		//  同时shutdownNow()中断所有正在运行的任务并立即关闭执行程序。
		stop(executorService);
	}

	static void stop(ExecutorService executor) {
		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			System.err.println("termination interrupted");
		}
		finally {
			if (!executor.isTerminated()) {
				System.err.println("killing non-finished tasks");
			}
			executor.shutdownNow();
			System.out.println("shutdown finished");
		}
	}
}
