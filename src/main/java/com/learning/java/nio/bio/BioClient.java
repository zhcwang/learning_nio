package com.learning.java.nio.bio;

import java.io.*;
import java.net.Socket;

/**
 * @Auther wang.zhc
 * @Date 2019/11/14 14:17
 * @Description
 */
public class BioClient {
    public static void main(String[] args) throws Exception {
        DataInputStream in = null;
        DataOutputStream out =null;
        try(Socket socket = new Socket("127.0.0.1", 8000)) {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("I am Client");
            String resp = in.readUTF();
            System.out.println("接收到服务器响应：" + resp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }
}
