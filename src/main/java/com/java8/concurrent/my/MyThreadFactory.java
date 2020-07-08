package com.java8.concurrent.my;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {
	
	private final String threadNamePrefix;
	private final AtomicInteger nextId = new AtomicInteger(1);
	
	/**
	 *
	 * @param threadNameByGroups 线程组的名称
	 */
	public MyThreadFactory(String threadNameByGroups) {
		this.threadNamePrefix = "From MyThreadFactory's " + threadNameByGroups + "-Worker-";
	}
	
	/**
	 * Constructs a new {@code Thread}.  Implementations may also initialize
	 * priority, name, daemon status, {@code ThreadGroup}, etc.
	 *
	 * @param r a runnable to be executed by new thread instance
	 * @return constructed thread, or {@code null} if the request to
	 * create a thread is rejected
	 */
	@Override
	public Thread newThread(Runnable r) {
		String name = threadNamePrefix + nextId.getAndDecrement();
		Thread thread = new Thread(null, r, name, 0);
		System.out.println(thread.getName() + " created");
		return thread;
	}
}
