package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * SUBSCRIBE连接处理
 */
public class Subscribe {
    private static final Logger LOGGER = LoggerFactory.getLogger(Subscribe.class);

    public void processSubscribe(Channel channel, MqttSubscribeMessage msg){

    }
}
