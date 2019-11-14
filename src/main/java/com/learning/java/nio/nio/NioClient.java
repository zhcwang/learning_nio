package com.learning.java.nio.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther wang.zhc
 * @Date 2019/11/14 15:13
 * @Description
 */
public class NioClient {

    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        //打开管道
        SocketChannel socketChannel = SocketChannel.open();
        //设置管道为非阻塞模式
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8000));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            SelectionKey key = null;
            while (it.hasNext()) {
                key = it.next();
                try {
                    SocketChannel client = (SocketChannel) key.channel();
                    if (key.isConnectable()) {
                        System.out.println("client: connectable");
                        if (client.isConnectionPending()){
                            client.finishConnect();
                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put((LocalDateTime.now() + " 连接成功").getBytes());
                            writeBuffer.flip();
                            client.write(writeBuffer);
                        }
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {
                        System.out.println("client: readable");
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int count = client.read(readBuffer);
                        if (count > 0) {
                            String receivedMsg = new String(readBuffer.array(), 0, count);
                            System.out.println(receivedMsg);
                        }
                    }
                } catch (Exception e) {
                    if (key != null) {
                        key.cancel();
                        if (key.channel() != null) {
                            //关闭该通道
                            key.channel().close();
                        }
                    }
                }
            }
            selectionKeys.clear();;
        }
    }
}
