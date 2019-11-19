package com.learning.java.nio.netty.demo;

import com.learning.java.nio.netty.demo.pojo.FormatTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 * @Auther wang.zhc
 * @Date 2019/11/19 17:21
 * @Description
 *  服务器POJO转换适配
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("server: encoder");
        FormatTime m = (FormatTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(19);
        System.out.println(m.getTime());
        encoded.writeCharSequence(m.getTime(), Charset.forName("UTF-8"));
        ctx.write(encoded, promise); // (1)
        ctx.flush();
    }
}
/*
public class TimeEncoder extends MessageToByteEncoder<FormatTime> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, FormatTime formatTime, ByteBuf out) throws Exception {
        System.out.println("server: encoder");
        out.writeCharSequence(formatTime.getTime(), Charset.forName("UTF-8"));
    }
}
*/
