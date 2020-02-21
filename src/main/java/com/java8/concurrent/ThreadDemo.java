package com.java8.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Java 从JDK 1.0开始支持Threads。在开始新线程之前，您必须指定此线程要执行的代码，通常称为任务task 。
 */
public class ThreadDemo {

	/**
	 * 这是通过实现Runnable- 一个定义单个void no-args方法的功能接口来完成的，run()
	 */
	@Test
	public void testRunnable() {
		// 1. lambad指定run的实现过程
		Runnable task = () -> {
			String name = Thread.currentThread() // 返回对当前正在执行的线程对象的引用。
					.getName(); // 返回此线程的名称。
			System.out.println("线程名字 ： " + name);
		};
		// 2. 调用实现1
		task.run();

		// 3. 首先，我们在开始一个新线程之前直接在进行上执行runnable。
		Thread thread = new Thread(task);
		thread.start();

		System.out.println("OVER");
	}

	@Test
	public void testRunnable2() {
		Runnable task = () -> {
			try {
				System.out.println("Foo " + Thread.currentThread().getName());
				Thread.sleep(1000);
				System.out.println("Bar " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(task);
		thread.start();
		// Foo Thread-1
	}

	@Test
	public void testRunnable3() {
		Runnable task = () -> {
			try {
				System.out.println("Foo " + Thread.currentThread().getName());
				TimeUnit.SECONDS.sleep(1);
				System.out.println("Bar " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Thread thread = new Thread(task);
		thread.start();
		// Foo Thread-1
	}
	
	@Test
	public void testRunnableAndCallable(){
		System.out.println(Thread.currentThread().getPriority());
		Runnable runnable = new MyThread();
		
		// Callable<Double> callable = () -> Math.random();
		IntStream.rangeClosed(1, 10)
				.forEach(i -> {
					System.out.println(Thread.currentThread().getPriority());
					Thread thread = new Thread(runnable, "runnable" + i);
					thread.start();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (thread.isInterrupted()==false) {    //该线程中断了吗？
						System.out.println("我偷偷地打扰一下你的睡眠");
						thread.interrupt();      //中断执行
					}
				});
		
	}
	
	@Test
	public void testTicket() {
		TicketThread mt= new TicketThread();
		new Thread(mt,"票贩子A").start() ;
		new Thread(mt,"票贩子B").start() ;
		new Thread(mt,"票贩子C").start() ;
	}
}

class MyThread implements Runnable {
	
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		System.out.println("*** 72个小时的疯狂我需要睡觉补充精力。——》" + Thread.currentThread().getName());
		try {
			Thread.sleep(10000);     //预计准备休眠10秒
			System.out.println("****** 睡足了，可以出去继续祸害别人了。");
		} catch (InterruptedException e) {
			System.out.println("不要打扰我睡觉，我会生气的。");
		}
	}
}

class TicketThread implements Runnable {
	private int ticket = 10 ;        //总票数为10张
	@Override
	public void run() {
		while (true) {
			if (this.ticket > 0) {
				try {
					Thread.sleep(1000);    //模拟网络延迟
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + "卖票，ticket = " + this.ticket--) ;
			} else {
				System.out.println("***** 票已经卖光了 *****") ;
				break ;
			}
		}
	}
}

