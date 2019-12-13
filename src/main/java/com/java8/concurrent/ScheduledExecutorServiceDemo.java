package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * ScheduledExecutorService能够在一定时间过后定期执行一些任务
 */
public class ScheduledExecutorServiceDemo {

	/**
	 * 安排定期执行的任务，执行者提供了两种方法scheduleAtFixedRate()和scheduleWithFixedDelay()。
	 * scheduleAtFixedRate:能够以固定的时间速率执行任务，例如每秒一次
	 */
	@Test
	public void testScheduledExecutorService() throws InterruptedException {
		// 执行器服务提供者
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Runnable task = () -> System.out.println("任务1 : " + System.nanoTime());
		// 计划在经过4秒的初始延迟后运行的任务
		// 调度任务会产生一种类型的特殊未来ScheduledFuture
		ScheduledFuture<?> schedule = executor.schedule(task, 4, TimeUnit.SECONDS);

		TimeUnit.MILLISECONDS.sleep(1000);
		// getDelay()检索剩余延迟的方法
		long milliseconds = schedule.getDelay(TimeUnit.MILLISECONDS);
		long seconds = schedule.getDelay(TimeUnit.SECONDS);
		long minutes = schedule.getDelay(TimeUnit.MINUTES);
		System.out.println("剩余延迟(毫秒): " + milliseconds);
		System.out.println("剩余延迟(秒): " + seconds);
		System.out.println("剩余延迟(分钟): " + minutes);

		// 能够以固定的时间速率执行任务，例如每秒一次
		// (线程，初始延迟，频率，延迟类型（秒，分，小时）)
		schedule = executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
		milliseconds = schedule.getDelay(TimeUnit.MILLISECONDS);
		seconds = schedule.getDelay(TimeUnit.SECONDS);
		minutes = schedule.getDelay(TimeUnit.MINUTES);
		System.out.println("剩余延迟(毫秒): " + milliseconds);
		System.out.println("剩余延迟(秒): " + seconds);
		System.out.println("剩余延迟(分钟): " + minutes);

		// 关闭执行器
		executor.shutdown();
	}

	/**
	 * 安排定期执行的任务，执行者提供了两种方法scheduleAtFixedRate()和scheduleWithFixedDelay()。
	 * scheduleAtFixedRate:能够以固定的时间速率执行任务，例如每秒一次
	 */
	@Test
	public void testScheduledExecutorServiceScheduleAtFixedRateAndScheduleWithFixedDelay() {
		// 执行器服务提供者
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
		Runnable task = () -> System.out.println("任务1 : " + System.nanoTime());

		// 能够以固定的时间速率执行任务，例如每秒一次
		// (线程，初始延迟，频率，延迟类型（秒，分，小时）)
		// scheduleAtFixedRate()没有考虑任务的实际持续时间。因此，如果您指定一秒的时间段，但任务需要2秒钟才能执行
		executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

		Runnable task2 = () -> {
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println("任务2 : " + System.nanoTime());
			} catch (InterruptedException e) {
				System.err.println("task interrupted");
			}
		};

		//此示例在执行结束和下一次执行开始之间调度具有一秒固定延迟的任务。初始延迟为零，任务持续时间为2秒。
		// 因此我们最终得到的执行间隔为0s，3s，6s，9s等等。
		// scheduleWithFixedDelay()如果你无法预测计划任务的持续时间，你可以使用，会看到很方便。
		executor.scheduleWithFixedDelay(task2, 0, 1, TimeUnit.SECONDS);
	}
}
