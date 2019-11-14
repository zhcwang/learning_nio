package com.learning.java.nio.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Auther wang.zhc
 * @Date 2019/11/14 14:17
 * @Description
 */
public class BioServer{

    private static Executor executor = Executors.newFixedThreadPool(8);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);
        while (true){
            Socket socket = serverSocket.accept();
            executor.execute(() -> {
                System.out.println("接收到客户端连接。");
                DataInputStream dis = null;
                DataOutputStream dos = null;
                try {
                    dis = new DataInputStream(socket.getInputStream());
                    System.out.println(String.format("接收到客户端请求，请求内容%s", dis.readUTF()));
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF("你好客户端，我已接收到你的请求。");
                    dos.flush();
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    try {
                        if (dis != null) dis.close();
                        if (dos != null) dos.close();
                        if (socket != null) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
