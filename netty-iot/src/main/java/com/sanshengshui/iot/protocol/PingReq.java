package com.sanshengshui.iot.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * @date 2018年10月21日01:45
 */
public class PingReq {
    public static final Logger LOGGER = LoggerFactory.getLogger(PingReq.class);

    public void processPingReq(Channel channel, MqttMessage msg){
        MqttMessage pingRespMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0), null, null);
        LOGGER.debug("PINGREQ - clientId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get());
        channel.writeAndFlush(pingRespMessage);

    }
}
