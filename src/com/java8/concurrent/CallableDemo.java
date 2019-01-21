package com.java8.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 除了Runnable执行程序支持另一种命名的任务Callable。
 * Callables就像runnables一样是功能接口，但是他返回的是一个值，不是void。
 */
public class CallableDemo {

	public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
		// testCallable01();
		// testCallable02();
		// testCallableInvokeAll();
		testCallableInvokeAny();
	}

	public static void testCallable01() throws ExecutionException, InterruptedException {
		// 这个lambda表达式定义了一个可调用的函数，它在休眠一秒后返回一个整数
		Callable<Integer> task = () -> {
			try {
				TimeUnit.SECONDS.sleep(1);
				return 100;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task interrupted", e);
			}
		};

		// 将Callables提交给执行者服务
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		// 由于submit()不等到任务完成，所以执行程序服务不能直接返回可调用的结果。
		// 如果执行程序需要能返回结果，Future可用于以后的某个时间点检索实际结果(比如延迟2s)
		Future<Integer> future = executorService.submit(task);
		// 在将可调用程序提交给执行程序之后，我们首先检查未来（future）是否已经完成执行isDone()
		System.out.println("future done? " + future.isDone());
		// 调用get()阻塞当前线程并并等待基础调用终止，直到调用完成后再返回实际结果100
		//
		Integer result = future.get();
		// 现在终于完成了未来（future）
		System.out.println("future done? " + future.isDone());
		System.out.print("result: " + result);
		// callable 与 ExecutorService紧密结合。如果关闭执行程序，则每个未终止的将来（future）都会抛出异常
		executorService.shutdownNow();
		future.get();

	}

	public static void testCallable02() throws ExecutionException, InterruptedException, TimeoutException {
		// 线程执行的内容
		Callable<String> task = () -> {
			// 延迟1s
			TimeUnit.SECONDS.sleep(2);
			return UUID.randomUUID().toString();
		};
		// 基础执行的服务者
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		// 立马执行线程，不会等到返回结果
		Future<String> future = executorService.submit(task);
		// 任何调用都future.get()将阻塞并等待基础调用终止。
		// 在最坏的情况下，可调用程序会永远运行 - 从而使您的应用程序无响应。
		// 您可以通过传递超时来简单地抵消这些情况：
		String s = future.get(1, TimeUnit.SECONDS);
		System.out.println(s);
		executorService.shutdown();
	}

	/**
	 * InvokeAll:执行程序使用invokeAll()方法支持一次批量提交多个callables。
	 * 此方法接受一组可调用对象并返回一个可回调的线程（Callable）列表。
	 * ExecutorService（执行器）：newWorkStealingPool()来创建执行器
	 * ForkJoinPool：ForkJoinPool它的工作方式与普通执行程序ThreadPoolExecutor略有不同。
	 * 而不是使用固定大小的线程池ForkJoinPools是为给定的并行度大小创建的，
	 * 默认情况下是主机CPU的可用核心数。
	 */
	public static void testCallableInvokeAll() throws InterruptedException {
		// 1.线程服务提供者
		ExecutorService executor = Executors.newWorkStealingPool();
		// 2.可回调的线程的列表
		List<Callable<String>> list = Arrays.asList(
				() -> {
					TimeUnit.SECONDS.sleep(3);
					return "任务01";
				},
				() -> "任务02",
				() -> {
					TimeUnit.SECONDS.sleep(2);
					return "任务03";
				}
		);
		// 3.批量执行所有的线程
		executor.invokeAll(list)
				.stream()
				.map(future -> {
					try {
						return future.get();
					} catch (Exception e) {
						e.printStackTrace();
						return "错误结果";
					}
				})
				.forEach(System.out::println);

	}

	/**
	 * InvokeAny : 批量提交callables的另一种方法invokeAny()是工作方式略有不同invokeAll()。
	 * 此方法不会返回将来的对象，而是阻塞，直到第一个callable终止并返回该callable的结果。
	 */
	public static void testCallableInvokeAny(){
		List<Callable<String>> listCallable = Arrays.asList(
				() -> {
					TimeUnit.SECONDS.sleep(5);
					return "任务01";
				},
				() -> "任务02",
				() -> {
					TimeUnit.SECONDS.sleep(2);
					return "任务03";
				}
		);

		// 线程基础服务者
		ExecutorService service = Executors.newWorkStealingPool();
		try {
			// 只要有第一个线程进来就阻塞其它线程，直到第一个线程返回结果，结束，返回，
			// 其它的线程都不在执行了
			String any = service.invokeAny(listCallable);
			System.out.println(any); // 任务02
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdown();
	}
}
