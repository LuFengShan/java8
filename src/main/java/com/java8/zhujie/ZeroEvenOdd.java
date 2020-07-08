package com.java8.zhujie;

import java.util.function.IntConsumer;

public class ZeroEvenOdd {
	private int n;
	
	public ZeroEvenOdd(int n) {
		this.n = n;
	}
	
	// printNumber.accept(x) outputs "x", where x is an integer.
	public void zero(IntConsumer printNumber) throws InterruptedException {
		System.out.print(0);
	}
	
	public void even(IntConsumer printNumber) throws InterruptedException {
	
	}
	
	public void odd(IntConsumer printNumber) throws InterruptedException {
	
	}
}
