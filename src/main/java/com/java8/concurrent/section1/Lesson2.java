package com.java8.concurrent.section1;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

import static java.util.Arrays.asList;

public class Lesson2 {
	public static void main(String[] args) {
		// 创建一个6棵数的集合，每一棵树有一个编号
		AppleTree[] appleTrees = AppleTree.newTreeGarden(300000);
		
		Callable<Void> applePicker1 = createApplePicker(appleTrees, 0, 100000, "Alex");
		Callable<Void> applePicker2 = createApplePicker(appleTrees, 100001, 200000, "Bob");
		Callable<Void> applePicker3 = createApplePicker(appleTrees, 200001, 300000, "Carol");
		
		ForkJoinPool.commonPool()
				.invokeAll(asList(applePicker1, applePicker2, applePicker3));
		
		System.out.println();
		System.out.println("All fruits collected!");
	}
	
	public static Callable<Void> createApplePicker(AppleTree[] appleTrees, int fromIndexInclusive, int toIndexExclusive,
	                                               String workerName) {
		return () -> {
			for (int i = fromIndexInclusive; i < toIndexExclusive; i++) {
				appleTrees[i].pickApples(workerName);
			}
			return null;
		};
	}
}
