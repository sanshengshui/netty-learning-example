package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * CONNECT连接处理
 */
public class Connect {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);

    public void processConnect(Channel channel, MqttConnectMessage msg){

    }
}
