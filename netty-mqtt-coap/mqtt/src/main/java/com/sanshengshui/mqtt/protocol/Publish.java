package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBLISH连接处理
 * @author james
 */
public class Publish {
    private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);

    public void processPublist(Channel channel, MqttPublishMessage msg){

    }
}
