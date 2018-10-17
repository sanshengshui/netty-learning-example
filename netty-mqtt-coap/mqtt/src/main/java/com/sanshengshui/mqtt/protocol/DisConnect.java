package com.sanshengshui.mqtt.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * DISCONNECT连接处理
 */
public class DisConnect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisConnect.class);

    public void processDisConnect(Channel channel, MqttMessage msg){
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        /**
         * TODO
         * 服务端在收到DISCONNECT报文时：
         * 必须丢弃任何与当前连接关联的未发布的遗嘱消息
         */
        channel.close();
    }
}
