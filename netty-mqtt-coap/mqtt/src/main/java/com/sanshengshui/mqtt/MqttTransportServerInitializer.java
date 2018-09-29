package com.sanshengshui.mqtt;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttTransportServerInitializer extends ChannelInitializer<SocketChannel> {
    private final int maxPayloadSize;

    public MqttTransportServerInitializer(int maxPayloadSize){
        this.maxPayloadSize = maxPayloadSize;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        log.info("mqtt server initial!");
        pipeline.addLast("decoder", new MqttDecoder(maxPayloadSize));
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);
//        pipeline.addLast("idleStateHandler", new IdleStateHandler(10,2,12, TimeUnit.SECONDS));
        MqttTransportHandler handler = new MqttTransportHandler(20);
        pipeline.addLast(handler);
        socketChannel.closeFuture().addListener(handler);
    }
}
