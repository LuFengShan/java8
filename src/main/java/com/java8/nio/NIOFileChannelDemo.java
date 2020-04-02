package com.java8.nio;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelDemo {
	
	/**
	 * 把一个内容写入到文件中
	 */
	@Test
	public void readStringWriteToFile() throws IOException {
		String str = "hello 王五";
		// 记住所在的流都是面向程序本身的，因为我们要写入文件，对于程序来说就是输出了，所以用输出流
		FileOutputStream fileOutputStream = new FileOutputStream("d:/file01.txt");
		
		// get channel
		FileChannel channel = fileOutputStream.getChannel();
		
		// create buffer
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put(str.getBytes());
		
		// 把缓存区中的数组写入通道，站在channel的角度，channel是把缓存区数据写入到通道
		byteBuffer.flip();
		channel.write(byteBuffer);
		
		
		System.out.println(new String(byteBuffer.array()));
		
		fileOutputStream.close();
	}
}
