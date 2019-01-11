/**
 * 2018-07-25
 */
package com.java8.socket;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author sgx
 * @version
 * @since V1.1.0
 */
public class TestClientSocket {
  public static void main(String[] args) throws Exception {
    for (int i = 0; i < 5; i++) {
      // Client申请连接到Server端上
      Socket socket = new Socket("127.0.0.1", 6666);
      // 连接上服务器端以后，就可以向服务器端输出信息和接收从服务器端返回的信息
      // 输出信息和接收返回信息都要使用流式的输入输出原理进行信息的处理
      // 这里是使用输出流OutputStream向服务器端输出信息
      OutputStream outputStream = socket.getOutputStream();
      DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
      dataOutputStream.writeUTF("sgx，你好 : " + (i + 1));
      Thread.sleep(5000);
    }
  }
}
