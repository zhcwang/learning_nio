package com.learning.java.nio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * @Auther wang.zhc
 * @Date 2019/11/14 14:51
 * @Description
 */
public class NioServer {

    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(8000), 1024);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("The server is start on port: 8000");
        while (true){
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            SelectionKey key = null;
            while (iterator.hasNext()){
                key = iterator.next();
                if (key.isAcceptable()){
                    System.out.println("server : acceptable");
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel sc = server.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()){
                    System.out.println("server : readable");
                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readed = sc.read(buffer);
                    if (readed > 0){
                        buffer.flip();
                        byte[] tmp = new byte[buffer.remaining()];
                        buffer.get(tmp);
                        System.out.println("服务器读取到信息：" + new String(tmp, StandardCharsets.UTF_8));
                        ByteBuffer outBuffer = ByteBuffer.allocate(1024);
                        outBuffer.put(("你好客户端：" + System.currentTimeMillis()).getBytes());
                        outBuffer.flip();
                        sc.write(outBuffer);
                    } else {
                        key.cancel();
                        sc.close();
                    }
                }
            }
            selectionKeys.clear();
        }
    }
}
