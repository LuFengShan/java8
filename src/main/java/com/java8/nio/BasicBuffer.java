package com.java8.nio;

import java.nio.IntBuffer;

/**
 * private int mark = -1;
 * private int position = 0; 数组的容量，下次要读取的位置
 * private int limit; 最大可以读取的容量
 * private int capacity; 最大的容量
 */
public class BasicBuffer {
	public static void main(String[] args) {
		IntBuffer allocate = IntBuffer.allocate(5);
		for (int i = 0; i < allocate.capacity(); i++) {
			allocate.put(i * 2);
		}
		
		allocate.flip();
		while (allocate.hasRemaining()) {
			System.out.println(allocate.get());
		}
		
	}
}
