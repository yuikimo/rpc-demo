package org.example.client;

import org.example.MyObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    // 服务端的主机名或IP地址
    static String hostName = "localhost";
    // 服务端监听的端口
    static int port = 12345;

    public static void main(String[] args) throws IOException {
        runWithObject();
    }

    public static void run() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outToServer = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outToServer, true);
        String messageToSend = "Hello, Server xhy!";
        out.println(messageToSend);

        socket.close();
        System.out.println("发送完毕");
    }

    public static void test() throws IOException {
        Socket socket = new Socket(hostName, port);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream stream = new DataOutputStream(outputStream);

        // 发送多个小消息，模拟粘包和半包问题
        String[] messages = {"Hello", "Server!", "How", "are", "you?"};

        for (String message : messages) {
            stream.writeInt(message.length());
            stream.writeBytes(message);
            outputStream.flush();
        }

        socket.close();
    }

    // 粘包问题
    public static void runWithStickyBag() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outToServer = socket.getOutputStream();

        // 发送多个小消息，模拟粘包和半包问题
        String[] messages = {"Hello", "Server!", "How", "are", "you?"};
        for (String message : messages) {
            outToServer.write(message.getBytes());
            // 确保立即发送
            outToServer.flush();
        }

        socket.close();
    }

    // 解决粘包
    public static void resolveRunWithStickyBag() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        // DataOutputStream : 将基本数据类型以字节流的形式写入输出流的类
        OutputStream outToServer = socket.getOutputStream();
        DataOutputStream outputStream = new DataOutputStream(outToServer);

        // 发送多个小消息，使用消息头和消息体避免粘包和半包
        String[] messages = {"Hello", "Server!", "How", "are", "you?"};
        for (String message : messages) {
            // 发送消息长度
            outputStream.writeInt(message.length());
            // 发送消息
            outputStream.writeBytes(message);
            // 确保立即发送
            outputStream.flush();
        }

        socket.close();
    }

    // 半包问题
    public static void runWithHalfPackage() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        OutputStream outToServer = socket.getOutputStream();
        PrintWriter out = new PrintWriter(outToServer);

        // 发送一个长消息，模拟半包问题
        String longMessage =
                "This is a very long message that will be split into two parts to simulate a half package issue.";
        out.println(longMessage);

        socket.close();
    }

    public static void runWithObject() throws IOException {
        Socket socket = new Socket(hostName, port);
        System.out.println("Connected to server at " + hostName + ":" + port);

        // 使用对象输出流发送对象
        OutputStream outToServer = socket.getOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(outToServer);

        // 创建要发送的对象
        MyObject myObject = new MyObject("Hello", "World");
        outputStream.writeObject(myObject);

        outputStream.close();
        socket.close();
    }
}
