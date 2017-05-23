package com.lzl.netty.chapter2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 抛弃服务
 * 处理服务端channel
 * Created by lzl on 2017/5/23.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     *  由于我们并不知道服务是不是正确，我们在抛弃消息之前打印消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 抛弃丢掉的数据
        /*ByteBuf byteBuf = (ByteBuf) msg;
        try{
            while (byteBuf.isReadable()){ // 1
                System.out.print((char) byteBuf.readByte());
                System.out.flush();
            }
        }finally {
            ReferenceCountUtil.release(msg);// 2
        }*/
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 出现异常 关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
