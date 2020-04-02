package com.java8.nio.groupchart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
	//定义相关的属性
	private final String HOST = "192.168.10.215"; // 服务器的ip
	private final int PORT = 6667; //服务器端口
	private Selector selector;
	private SocketChannel socketChannel;
	private String username;
	
	public GroupChatClient() {
		try {
			selector = Selector.open();
			socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
			//得到username
			username = socketChannel.getLocalAddress().toString().substring(1);
			System.out.println(username + " is ok...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendInfo(String msg) {
		msg = username + " : " + msg;
		try {
			socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readInfoByServer() {
		try {
			int readChannels = selector.select();
			if (readChannels > 0) {
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					
					SelectionKey key = iterator.next();
					if (key.isReadable()) {
						//得到相关的通道
						SocketChannel sc = (SocketChannel) key.channel();
						//得到一个Buffer
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						//读取
						sc.read(buffer);
						//把读到的缓冲区的数据转成字符串
						String msg = new String(buffer.array());
						System.out.println(msg.trim());
					}
				}
				iterator.remove(); //删除当前的selectionKey, 防止重复操作
			} else {
				System.out.println("没有可以用的通道...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//启动我们客户端
		GroupChatClient chatClient = new GroupChatClient();
		
		//启动一个线程, 每个3秒，读取从服务器发送数据
		new Thread() {
			public void run() {
				
				while (true) {
					chatClient.readInfoByServer();
					try {
						Thread.currentThread().sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		//发送数据给服务器端
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			chatClient.sendInfo(s);
		}
	}
}
