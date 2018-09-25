package com.sanshengshui.mqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttTransportHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<Future<? super Void>> {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {

    }
}
