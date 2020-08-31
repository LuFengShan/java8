package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 计算信号量。虽然锁通常授予对变量或资源的独占访问权，但信号量能够维护整套许可。这在您必须限制对应用程序某些部分的并发访问量的不同情况下非常有用。
 */
public class SemaphoreDemo {


	@Test
	public void test1() {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		// 大小为5的信号量，从而将并发访问限制为5
		Semaphore semaphore = new Semaphore(2);

		Runnable longRunningTask = () -> {
			try {
				// 后续调用都会tryAcquire()经过一秒的最大等待超时
				semaphore.acquire();
				System.out.println("longRunningTask Semaphore acquired");
				ConcurrentUtils.sleep(1);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				semaphore.release();
			}
		};
		Runnable longRunningTask1 = () -> {
			try {
				semaphore.acquire();
				System.out.println("longRunningTask1 Semaphore acquired");
				ConcurrentUtils.sleep(3);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				semaphore.release();
			}
		};

		// 执行程序可以同时运行10个任务
		IntStream.range(0, 10)
				.forEach(i -> {
					if (i % 2 == 0) {
						executor.submit(longRunningTask);
					} else {
						executor.submit(longRunningTask1);
					}
				});

		ConcurrentUtils.stop(executor);
	}

	@Test
	public void test() {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 大小为5的信号量，从而将并发访问限制为5
		Semaphore semaphore = new Semaphore(2);

		Runnable longRunningTask = () -> {
			boolean permit = false;
			try {
				// 后续调用都会tryAcquire()经过一秒的最大等待超时
				permit = semaphore.tryAcquire(3, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("longRunningTask Semaphore acquired");
					ConcurrentUtils.sleep(5);
				} else {
					System.out.println("longRunningTask Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};
		Runnable longRunningTask1 = () -> {
			boolean permit = false;
			try {
				// 后续调用都会tryAcquire()经过一秒的最大等待超时
				permit = semaphore.tryAcquire(3, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("longRunningTask1 Semaphore acquired");
					ConcurrentUtils.sleep(5);
				} else {
					System.out.println("longRunningTask1 Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};

		// 执行程序可以同时运行10个任务
		IntStream.range(0, 10)
				.forEach(i -> {
					if (i % 2 == 0) {
						executor.submit(longRunningTask);
					} else {
						executor.submit(longRunningTask1);
					}
				});

		ConcurrentUtils.stop(executor);
	}


	@Test
	public void testDemo() {
		ExecutorService executor = Executors.newWorkStealingPool();
		// 大小为5的信号量，从而将并发访问限制为5
		Semaphore semaphore = new Semaphore(4);
		Callable<Integer> semaphoreTask = new SemaphoreTask(semaphore);

		// 执行程序可以同时运行10个任务
		List<Future<Integer>> list = new ArrayList<>(20);
		for (int i = 0; i < 20; i++) {
			Future<Integer> future = executor.submit(semaphoreTask);
			list.add(future);
			if (i % 4 == 0) {
				System.out.println("当前坐标：" + i + "->");
				try {
					for (int j = 0; j < list.size(); j++) {
						Integer integer = list.get(j).get();
						System.out.println(integer);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				list.removeAll(list);
				System.out.println("**********************");
			}
		}
		if (Objects.nonNull(list) && list.size() > 0) {
			try {
				for (int j = 0; j < list.size(); j++) {
					Integer integer = list.get(j).get();
					System.out.println(integer);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			list.removeAll(list);
		}
//		list.forEach(System.out::println);

		ConcurrentUtils.stop(executor);
	}

	@Test
	public void testDemo1() {
		boolean isCancle = false;
		ExecutorService executor = Executors.newWorkStealingPool();
		// 大小为5的信号量，从而将并发访问限制为5
		Semaphore semaphore = new Semaphore(4);


		// 执行程序可以同时运行10个任务
		List<Future<StringBoolean>> list = new ArrayList<>(200);
		Future<StringBoolean> future;
		StringBoolean stringBoolean1;
		for (int i = 0; i < 99; i++) {
			if (isCancle) {
				break;
			}
			stringBoolean1 = new StringBoolean("15210776053->" + i);
			future = executor.submit(new SemaphoreTaskByStringBoolean(semaphore, stringBoolean1));
			list.add(future);
			if (i % 4 == 0) {
				System.out.println("当前坐标：" + i + "->");
				try {
					for (int j = 0; j < list.size(); j++) {
						StringBoolean stringBoolean = list.get(j).get(3, TimeUnit.SECONDS);
						stringBoolean.setEnable(false);
						System.out.println(stringBoolean.toString());
					}
				} catch (InterruptedException | ExecutionException | TimeoutException e) {
					e.printStackTrace();
				}
				list.removeAll(list);
				System.out.println("**********************");
			}
			if (i == 21) {
				isCancle = true;
			}
		}
		if (Objects.nonNull(list) && list.size() > 0) {
			System.out.println("最近的任务处理：");
			try {
				for (int j = 0; j < list.size(); j++) {
					StringBoolean stringBoolean = list.get(j).get();
					stringBoolean.setEnable(false);
					System.out.println(stringBoolean.toString());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			list.removeAll(list);
		}

		ConcurrentUtils.stop(executor);
	}
}

class SemaphoreTask implements Callable<Integer> {
	private Semaphore semaphore;

	public SemaphoreTask(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	@Override
	public Integer call() throws Exception {
		try {
			semaphore.acquire();
			System.out.println("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3 - semaphore.availablePermits()) + "个并发");
			ConcurrentUtils.sleep(1);
			return ThreadLocalRandom.current().nextInt(1, 20);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		} finally {
			System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
			semaphore.release();
			System.out.println("线程" + Thread.currentThread().getName() + "离开，当前可用凭证：" + semaphore.availablePermits());
		}
	}
}

class SemaphoreTaskByStringBoolean implements Callable<StringBoolean> {
	private Semaphore semaphore;
	private StringBoolean stringBoolean;

	public SemaphoreTaskByStringBoolean(Semaphore semaphore, StringBoolean stringBoolean) {
		this.semaphore = semaphore;
		this.stringBoolean = stringBoolean;
	}

	@Override
	public StringBoolean call() throws Exception {
		try {
			semaphore.acquire();
			System.out.println("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3 - semaphore.availablePermits()) + "个并发");
			ConcurrentUtils.sleep(1);
			return this.stringBoolean;
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		} finally {
			System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
			semaphore.release();
			System.out.println("线程" + Thread.currentThread().getName() + "离开，当前可用凭证：" + semaphore.availablePermits());
		}
	}
}

class StringBoolean {
	private String phone;
	private Boolean enable = true;


	public StringBoolean() {
	}

	public StringBoolean(String phone) {
		this.phone = phone;
	}

	public StringBoolean(String phone, Boolean enable) {
		this.phone = phone;
		this.enable = enable;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("StringBoolean{");
		sb.append("phone='").append(phone).append('\'');
		sb.append(", enable=").append(enable);
		sb.append('}');
		return sb.toString();
	}
}
