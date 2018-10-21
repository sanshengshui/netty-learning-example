package com.sanshengshui.iot.protocol;

import com.sanshengshui.iot.common.message.GrozaDupPublishMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * @date 2018年10月21日 09:02
 * PUBACK连接处理
 */
public class PubAck {
    public static final Logger LOGGER = LoggerFactory.getLogger(PubAck.class);

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    public PubAck(GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService){
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
    }

    public void processPubAck(Channel channel, MqttMessageIdVariableHeader variableHeader){
        int messageId = variableHeader.messageId();
        LOGGER.debug("PUBACK - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        grozaDupPublishMessageStoreService.remove((String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);

    }
}
