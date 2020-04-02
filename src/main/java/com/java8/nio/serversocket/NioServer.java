package com.java8.nio.serversocket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NioServer {
	@Test
	public void nioServer() throws IOException {
		//1.1 创建ServerSocketChannel -> ServerSocket
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		//1.2 绑定一个端口6666, 在服务器端监听
		serverSocketChannel.socket().bind(new InetSocketAddress(6666));
		//1.3 设置为非阻塞模式
		serverSocketChannel.configureBlocking(false);
		
		//2.1 得到一个Selecor对象
		Selector selector = Selector.open();
		//2.2 把serversocketchannel注册到select中，事件为连接事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		// 3.1 循环等待客户端连接
		while (true) {
			int select = selector.select(1000);
			System.out.println("selector中key的数量 ： " + select);
			if (select == 0) { // wait 1s if no event 说明没有事件发生
				//System.out.println("server wait 1s, with out accept");
				continue;
			}
			
			// 如果返回的selectionkeys数量大于0，说明有事件发生了
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			System.out.println(selectionKeys.size());
			// iterator selectionkeys
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
			while (keyIterator.hasNext()) {
				// 获取各个事件
				SelectionKey selectionKey = keyIterator.next();
				// if selectionkey is acctept
				if (selectionKey.isAcceptable()) {
					// 1.1 create socketchannel 客户端和服务的通道
					SocketChannel socketChannel = serverSocketChannel.accept();
					System.out.println("客户端连接成功 生成了一个 socketChannel " + socketChannel.hashCode());
					// 1.2 设置成非阻塞
					socketChannel.configureBlocking(false);
					
					// 2.1 把socketChannel注册到select
					// 网络编程都是字节缓存
					socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
					
					System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size());
					
				}
				
				// if selectionkey is read
				if (selectionKey.isReadable()) {
					// 1. 通过key反向获取channel
					SocketChannel channel = (SocketChannel) selectionKey.channel();
					// 2. 获取这个通道关联的BUFFER
					ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
					channel.read(buffer);
					System.out.println("form 客户端 " + new String(buffer.array()));
				}
				
				// 手动把当前的key从集合中删除
				keyIterator.remove();
			}
		}
		
		
	}
	
	@Test
	public void nioClient() throws IOException, InterruptedException {
		//得到一个网络通道
		SocketChannel socketChannel = SocketChannel.open();
		//设置非阻塞
		socketChannel.configureBlocking(false);
		//提供服务器端的ip 和 端口
		InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
		//连接服务器
		if (! socketChannel.connect(inetSocketAddress)) {
			
			while (! socketChannel.finishConnect()) {
				System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
			}
		}
		
		//...如果连接成功，就发送数据
		TimeUnit.SECONDS.sleep(1000L);
		String str = "hello, 1254ghhh秋天不回来~";
		//Wraps a byte array into a buffer
		ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
		//发送数据，将 buffer 数据写入 channel
		socketChannel.write(buffer);
		System.in.read();
	}
}
