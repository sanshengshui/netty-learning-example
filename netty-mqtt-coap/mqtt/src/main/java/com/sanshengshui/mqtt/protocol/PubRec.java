package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PUBREC连接处理
 * @author james
 */
public class PubRec {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubRec.class);

    public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader){

    }
}
