package com.sanshengshui.iot.protocol;

import com.sanshengshui.iot.common.message.GrozaDupPubRelMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james
 * @date 2018年10月21日09:08
 * PUBCOMP连接处理
 */
public class PubComp {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubComp.class);

    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    public PubComp(GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService){
        this.grozaDupPubRelMessageStoreService = grozaDupPubRelMessageStoreService;
    }

    public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader){
        int messageId = variableHeader.messageId();
        LOGGER.debug("PUBCOMP - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        grozaDupPubRelMessageStoreService.remove((String)channel.attr(AttributeKey.valueOf("clientId")).get(), variableHeader.messageId());
    }
}
