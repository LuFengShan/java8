package com.java8.stack;

public class StackDemo {
	public static void main(String[] args) {
		method01();
	}
	
	public static void method01() {
		method02();
	}
	
	public static void method02() {
		System.out.println("over02");;
	}
}
