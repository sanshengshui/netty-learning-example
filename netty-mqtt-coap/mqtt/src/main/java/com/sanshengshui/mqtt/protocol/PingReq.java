package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * PINGREQ连接处理
 */
public class PingReq {
    private static final Logger LOGGER = LoggerFactory.getLogger(PingReq.class);

    public void processPingReq(Channel channel, MqttMessage msg){
        MqttMessage pingPespMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP,false, MqttQoS.AT_MOST_ONCE,false,0),
                null,
                null
        );
        LOGGER.debug("PINGREQ - clientId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get());
        channel.writeAndFlush(pingPespMessage);

    }
}
