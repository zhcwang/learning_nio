package com.learning.java.nio.netty.demo;

import com.learning.java.nio.netty.demo.pojo.FormatTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sun.java2d.pipe.SpanIterator;

import java.nio.charset.Charset;

/**
 * @Auther wang.zhc
 * @Date 2019/11/19 16:50
 * @Description
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 第一种解决粘包的方式，自行维护buf计数器
     */
    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(19); // (1)
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); // (1)
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      /*  ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m); // (2)
        m.release();
        if(buf.readableBytes() >= 19){
            CharSequence charSequence = ((ByteBuf) buf).readCharSequence(19, Charset.forName("UTF-8"));
            System.out.println(charSequence);
            ctx.close();
        }*/
        System.out.println("client:handler");
        System.out.println(((FormatTime)msg).getTime());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

}
