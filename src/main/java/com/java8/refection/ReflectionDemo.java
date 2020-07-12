package com.java8.refection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionDemo {
	private int age;
	private String name;

	public ReflectionDemo() {
	}

	public ReflectionDemo(int age, String name) {
		this.age = age;
		this.name = name;
	}

	public static String staticMethod() {
		return "staticMethod";
	}

	private String say() {
		return "she and he";
	}


	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		// 1. 调用对象的getClass
		ReflectionDemo reflectionDemo = new ReflectionDemo();
		Class<? extends ReflectionDemo> rd1 = reflectionDemo.getClass();
		// 2. 调用某个对象的class属性
		Class<ReflectionDemo> rd2 = ReflectionDemo.class;
//		Method[] methods = rd2.getMethods();
//		for (Method method: methods) {
//			System.out.println(method.getName());
//		}
		Method[] declaredMethods = rd2.getDeclaredMethods();
		for (Method method: declaredMethods) {
			System.out.println(method);
		}
		System.out.println("****************************");
		// 3. 使用forName属性
		Class<?> rd3 = Class.forName("com.java8.refection.ReflectionDemo");
//		methods = rd3.getMethods();
//		for (Method method: methods) {
//			System.out.println(method.getName());
//		}
		declaredMethods = rd3.getDeclaredMethods();
		for (Method method: declaredMethods) {
			System.out.println(method);
		}
		Field[] declaredFields = rd3.getDeclaredFields();
		for (Field method: declaredFields) {
			System.out.println(method);
		}

		Constructor<?>[] constructors = rd3.getConstructors();
		for (Constructor constructor: constructors) {
			System.out.println(constructor);
		}

		ReflectionDemo o = (ReflectionDemo) rd3.newInstance();
		System.out.println(o.say());

	}
}
