package com.lzl.netty.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃任何进入的数据
 * Created by lzl on 2017/5/23.
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    /**
     * 1 . 用来处理I/O操作的多线程事件循环器，Netty提供了许多EventLoopGroup的实现来处理不同的传输
     * 2 . 启动NIO服务的辅助启动类，可以在这个服务中直接使用channel
     * 3 . 这里我们使用NioServerSocketChannel类来举例一个新的channel如何接收进来的连接
     * 4 . 这个事件处理类经常会被用来处理一个最近接收的channel,ChannelInitializer 是一个特殊的处理类，他的目的是帮助使用者配置一个新的channel
     * 或许你会增加一些处理类比如DiscardServerHandler来配置一个新的channel或者对应的ChannelPipeline来实现你的网络程序。
     * 当你的程序变复杂的时候你可能会增加更多的处理类到pipeline上，然后提取这些匿名类到最顶层的类上。
     * 5 . 指定channel的配置参数
     * 6 . option是提供给NioServerSocketChannel 用来接收进来的连接，childOption是提供给父管道ServerChannel接收到的连接，在这个例子中也是NioServerSccketChannel
     * 7 . 绑定端口，启动服务，可以多次调用bind方法（基于不同绑定地址）
     * 8 . 可以用telnet localhost 9090 来验证服务是否开启
     * @throws Exception
     */
    public void run() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();// 1
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); // 2
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) // 3
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 4
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128) // 5
                    .childOption(ChannelOption.SO_KEEPALIVE,true); // 6

            // 绑定端口，接手进来的连接
            ChannelFuture f = bootstrap.bind(port).sync();// 7

            // 等待服务器socket 关闭
            // 在这个例子中不会发生，你可以优雅的关闭你的服务器
            f.channel().closeFuture().sync();
        }catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if(args.length > 0 ){
            port = Integer.parseInt(args[0]);
        }else {
            port = 9090;
        }
        new DiscardServer(9090).run();
    }
}
