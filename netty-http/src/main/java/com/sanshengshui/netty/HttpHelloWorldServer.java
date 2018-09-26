package com.sanshengshui.netty;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class HttpHelloWorldServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelloWorldServer.class);

    static final int PORT = 8888;

    public static void main(String[] args) throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childOption(ChannelOption.TCP_NODELAY,true);
            b.childOption(ChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpHelloWorldServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();
            logger.info("Netty http server listening on port "+ PORT);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
