package com.sanshengshui.iot.protocol;

import com.sanshengshui.iot.common.message.DupPubRelMessageStore;
import com.sanshengshui.iot.common.message.GrozaDupPubRelMessageStoreService;
import com.sanshengshui.iot.common.message.GrozaDupPublishMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubRec {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubRel.class);

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    public PubRec(GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService,
                  GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService){
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
        this.grozaDupPubRelMessageStoreService = grozaDupPubRelMessageStoreService;
    }

    public void processPubRec(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        MqttMessage pubRelMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(variableHeader.messageId()),
                null);
        LOGGER.debug("PUBREC - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), variableHeader.messageId());
        grozaDupPublishMessageStoreService.remove((String) channel.attr(AttributeKey.valueOf("clientId")).get(), variableHeader.messageId());
        DupPubRelMessageStore dupPubRelMessageStore = new DupPubRelMessageStore().setClientId((String) channel.attr(AttributeKey.valueOf("clientId")).get())
                .setMessageId(variableHeader.messageId());
        grozaDupPubRelMessageStoreService.put((String) channel.attr(AttributeKey.valueOf("clientId")).get(), dupPubRelMessageStore);
        channel.writeAndFlush(pubRelMessage);
    }
}
