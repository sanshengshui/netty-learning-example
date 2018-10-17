package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * PUBCOMP连接处理
 */
public class PubComp {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubComp.class);

    public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader){

    }
}
