package com.java8.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelDemo {
	public static void main(String[] args) throws Exception {
		RandomAccessFile aFile = new RandomAccessFile("D:\\公司大屏常用\\impala tables.txt", "rw");
		FileChannel inChannel = aFile.getChannel();
		
		// 分配一个1024字节容量的容器
		//create buffer with capacity of 48 bytes
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		buf.putDouble(1258589D);
		//read into buffer.
		int bytesRead = inChannel.read(buf);
		while (bytesRead != - 1) {
			
			//make buffer ready for read
			buf.flip();
			
			while (buf.hasRemaining()) {
				// read 1 byte at a time
				System.out.print((char) buf.get());
			}
			
			buf.clear(); //make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}
}
