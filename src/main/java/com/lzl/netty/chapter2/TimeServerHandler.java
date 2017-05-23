package com.lzl.netty.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by lzl on 2017/5/23.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception { // 1
        final ByteBuf byteBuf = ctx.alloc().buffer(4); // 2

        byteBuf.writeInt((int) (System.currentTimeMillis()/ 1000L +2208988800L));

        final ChannelFuture channelFuture = ctx.writeAndFlush(byteBuf);

        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                assert channelFuture == future;
                ctx.close();

            }
        });// 4

    }

}
