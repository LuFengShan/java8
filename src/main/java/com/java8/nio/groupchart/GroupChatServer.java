package com.java8.nio.groupchart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * 
 * @version V1.1.0
 * @Author sgx
 * @Date 2020/4/1 10:25
 **/
public class GroupChatServer {
	
	// 定义一个selector
	private Selector selector;
	// 定义一个服务器
	private ServerSocketChannel serverSocketChannel;
	// 定义服务的接入端口
	private static final int SERVER_PORT = 6667;
	
	// 完成初始化工作
	public GroupChatServer() {
		try {
			//1.0 初始化serverSocketChannel，作为一个服务，当有连接进来的时候，负责创建一个socketChannel通道，
			serverSocketChannel = ServerSocketChannel.open();
			//1.1 绑定服务的端口
			serverSocketChannel.socket().bind(new InetSocketAddress(SERVER_PORT));
			//1.2 设置这个serverSocketChannel服务是非阻塞的
			serverSocketChannel.configureBlocking(false);
			//1.3 当有客户端接入的时候，serverSocketChannel要负责创建一个连接，而且是非阻塞的连接
			// 所以这个serverSocketChannel要加入我们的selector中，来负责监听serverSocketChannel通道是否有事件发生
			//1.4 创建需要的监听selector
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 监听进来的线程
	 *
	 * @version V1.1.0
	 * @Author sgx
	 * @Date 2020/4/1 9:16
	 **/
	public void listen() {
		while (true) {
			// 得到有事件发生的通道的数量，如果没有发生则是0
			int eventCountByChannel = 0;
			try {
				eventCountByChannel = selector.select();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 如果大于0，则说明有些通道内有读写事件发生
			if (eventCountByChannel > 0) {
				// 每一个通道都有一个唯一的key和这个通道绑定
				// 遍历所在的通道，找出有事件发生的通道
				Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
				while (selectionKeyIterator.hasNext()) {
					// 取出通道对应的key
					SelectionKey selectionKey = selectionKeyIterator.next();
					// 判断这个key的属性
					// 如果这个key是请求连接的
					if (selectionKey.isAcceptable()) {
						try {
							// 服务建立一个通道连接客户端，并且也要把这个通道设置成非阻塞的，而且也要把这个通道
							// 注册进selector中，让selector负责监听
							SocketChannel socketChannel = serverSocketChannel.accept();
							socketChannel.configureBlocking(false);
							// 注意，这个因为是监听方法，所以只会从缓存中读取数据
							socketChannel.register(selector, SelectionKey.OP_READ);
							System.out.println(socketChannel.getRemoteAddress() + " 上线");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					// 如果监听的通道是有数据的，那们就要读取出来
					if (selectionKey.isReadable()) {
						// 获取到这个通道，必须进行向下转型
						SocketChannel channel = (SocketChannel) selectionKey.channel();
						// 网络传输过程中都是以字节进行传输的,建立一个缓存块
						ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
						// 把通道中的数据读取到缓存块中
						// 如果readContext的内容大于0，则读取到了数据，否则可能为0或-1
						int readContext = 0;
						try {
							readContext = channel.read(byteBuffer);
							if (readContext > 0) {
								// 把字节转换成我们能识别的字符
								String message = new String(byteBuffer.array());
								System.out.println("来自客户端的消息 ： " + message);
								// 服务端把数据接收以后，我们要把这个消息转发给其它的客户端
								System.out.println("服务器转发消息中...");
								System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
								for (SelectionKey key : selector.keys()) {
									Channel otherChannel = key.channel();
									if (otherChannel instanceof SocketChannel
											&& ! Objects.equals(otherChannel, channel)) {
										SocketChannel dest = (SocketChannel) otherChannel;
										//将msg 存储到buffer
										ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
										//将buffer 的数据写入 通道
										dest.write(buffer);
									}
									
								}
							}
						} catch (IOException e) {
							try {
								System.out.println(channel.getRemoteAddress() + " 离线了..");
								selectionKey.cancel();
								channel.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							
						}
					}
					//当前的key 删除，防止重复处理
					selectionKeyIterator.remove();
				}
			} else {
				System.out.println("等待....");
			}
		}
		
	}
	
	public static void main(String[] args) {
		new GroupChatServer().listen();
	}
}
