package com.sanshengshui.iot.server;

import com.sanshengshui.iot.protocol.ProtocolProcess;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息处理
 */
@Slf4j
@ChannelHandler.Sharable
public class MqttTransportHandler extends SimpleChannelInboundHandler<MqttMessage> {
    @Autowired
    private ProtocolProcess protocolProcess;

    private final int keepaliveSeconds;

    public MqttTransportHandler(int keepaliveSeconds) {
        this.keepaliveSeconds = keepaliveSeconds;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
