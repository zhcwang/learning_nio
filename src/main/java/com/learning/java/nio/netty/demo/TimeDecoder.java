package com.learning.java.nio.netty.demo;

import com.learning.java.nio.netty.demo.pojo.FormatTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Auther wang.zhc
 * @Date 2019/11/19 17:05
 * @Description
 *  第二种解决粘包的方式，继承 ByteToMessageDecoder，并注册到pipeline
 */
public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("client:decoded");
        if (in.readableBytes() < 19) {
            return; // (3)
        }
        /**
         * 模型转换
         */
        CharSequence charSequence = in.readCharSequence(19, Charset.forName("UTF-8"));
        out.add(new FormatTime(charSequence.toString())); // (4)
    }
}
