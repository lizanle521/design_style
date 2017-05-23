package com.lzl.netty.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by lzl on 2017/5/23.
 */
public class TimeClient {
    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();//1
            bootstrap.group(workerGroup);//2
            bootstrap.channel(NioSocketChannel.class);//3
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);// 4

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            //启动客户端
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 等待连接关闭
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
