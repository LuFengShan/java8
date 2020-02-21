package com.java8.concurrent.section2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Lesson1 {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		demoFutureWithCallable();
		// runnable是不会返回结果的，不能判断是否已经结束和取消
		demoCallableVsRunnable();
	}

	public static void demoFutureWithCallable() throws InterruptedException, ExecutionException {
		System.out.println();
		System.out.println("Demo Future with Callable");
		ExecutorService pool = Executors.newCachedThreadPool();

		Future<Pizza> pizzaPickupOrder = pool.submit(() -> {
			System.out.println("   Restaurant> 西红柿片");
			System.out.println("   Restaurant> 洋葱丝");
			System.out.println("   Restaurant> 撒上番茄酱和配料");
			System.out.println("   Restaurant> 烤披萨");
			TimeUnit.MILLISECONDS.sleep(1000);
			return new Pizza();
		});

		System.out.println("Me: 打电话给我哥哥");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.println("Me: 遛狗");

		// Try this: pizzaPickupOrder.cancel(true);
		if (pizzaPickupOrder.isCancelled()) {
			System.out.println("Me: 比萨饼取消了，点别的吧");
			System.out.println("pizzaPickupOrder.isDone(): " + pizzaPickupOrder.isDone());
		} else if (!pizzaPickupOrder.isDone()) {
			System.out.println("Me: 看电视节目");
		}
		Pizza pizza = pizzaPickupOrder.get();

		System.out.println("Me: 吃披萨: " + pizza);

		pool.shutdown();
		System.out.println();
		System.out.println();
	}

	public static void demoCallableVsRunnable() throws InterruptedException, ExecutionException {
		System.out.println();
		System.out.println("Demo: Callable vs Runnable");
		ExecutorService pool = Executors.newCachedThreadPool();

		Runnable makePizza = () -> {
			System.out.println("   Restaurant> 西红柿片");
			System.out.println("   Restaurant> 洋葱丝");
			System.out.println("   Restaurant> 撒上番茄酱和配料");
			System.out.println("   Restaurant> 烤披萨");
			// Compare to Callable: need to handle exception here
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Compare to Callable: nothing to return
		};

		// compare to submit(Callable): Future<?> here vs Future<T> there
		Future<?> pizzaPickupOrder = pool.submit(makePizza);

		// try this: pool.execute(makePizza);

		System.out.println("Me: Calling my brother");
		TimeUnit.MILLISECONDS.sleep(200);
		System.out.println("Me: Walk the dog");

		Object pizza = pizzaPickupOrder.get(); // null
		System.out.println("Me: Eat the pizza: " + pizza);

		pool.shutdown();
	}

	public static class Pizza {

		@Override
		public String toString() {
			return "经典披萨";
		}

	}

}
