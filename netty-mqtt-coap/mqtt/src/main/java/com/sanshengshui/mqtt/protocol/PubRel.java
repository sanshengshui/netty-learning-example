package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBREL连接处理
 * @author james
 */
public class PubRel {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubRel.class);

    public void processPubRel(Channel channel, MqttMessageIdVariableHeader variableHeader){

    }
}
