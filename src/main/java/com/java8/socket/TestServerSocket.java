/**
 * 2018-07-25
 */
package com.java8.socket;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author sgx
 * @version
 * @since V1.1.0
 */
public class TestServerSocket {
  public static void main(String[] args) throws Exception {
    // 创建一个服务，绑定端口为6666
    ServerSocket ss = new ServerSocket(6666);
    while (true) {
      // 如果有客户端请求连接了，就创建一个Socket（插座）对象
      Socket accept = ss.accept();
      System.out.println("A Client Connected!" + accept.getInetAddress());
      /* 使用InputStream流接收从客户端发送过来的信息，使用DataInputStream数据流处理接收到的信息 */
      DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());
      // 使用readUTF(方法将接收到的信息全部读取出来
      String readUTF = dataInputStream.readUTF();
      System.out.println(readUTF);
    }
  }

}
