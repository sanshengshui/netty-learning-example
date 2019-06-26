package com.sanshengshui.persistence.netty;

import com.sanshengshui.persistence.device.DeviceService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@ConfigurationProperties(prefix = "netty")
@Slf4j
public class NettServer {

    @Value("${netty.bind_address}")
    private String host;
    @Value("${netty.bind_port}")
    private Integer port;

    @Value("${netty.boss_group_thread_count}")
    private Integer bossGroupThreadCount;
    @Value("${netty.worker_group_thread_count}")
    private Integer workerGroupThreadCount;

    @Autowired
    private DeviceService deviceService;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() throws Exception {

        log.info("Starting Netty Server");
        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        NettyServerHandler nettyServerHandler = new NettyServerHandler(deviceService);
                        pipeline.addLast(nettyServerHandler);
                    }
                });

        serverChannel = b.bind(host, port).sync().channel();


        log.info("Netty Server started!");
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        log.info("Stopping Netty Server!");
        try {
            serverChannel.close().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("MQTT transport stopped!");
    }
}
