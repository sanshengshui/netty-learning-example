package com.sanshengshui.iot.protocol;

import com.sanshengshui.iot.common.message.*;
import com.sanshengshui.iot.common.session.GrozaSessionStoreService;
import com.sanshengshui.iot.common.subscribe.GrozaSubscribeStoreService;
import com.sanshengshui.iot.common.subscribe.SubscribeStore;
import com.sanshengshui.iot.internal.InternalMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Publish {

    private GrozaSessionStoreService grozaSessionStoreService;

    private GrozaSubscribeStoreService grozaSubscribeStoreService;

    private GrozaMessageIdService grozaMessageIdService;

    private GrozaRetainMessageStoreService grozaRetainMessageStoreService;

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    private GrozaKafkaService grozaKafkaService;


    public Publish(GrozaSessionStoreService grozaSessionStoreService,
                   GrozaSubscribeStoreService grozaSubscribeStoreService,
                   GrozaMessageIdService grozaMessageIdService,
                   GrozaRetainMessageStoreService grozaRetainMessageStoreService,
                   GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService,
                   GrozaKafkaService grozaKafkaService){
        this.grozaSessionStoreService = grozaSessionStoreService;
        this.grozaSubscribeStoreService = grozaSubscribeStoreService;
        this.grozaMessageIdService = grozaMessageIdService;
        this.grozaRetainMessageStoreService = grozaRetainMessageStoreService;
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
        this.grozaKafkaService = grozaKafkaService;
    }

    public void processPublish(Channel channel, MqttPublishMessage msg) {
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        // QoS=0
        if (msg.fixedHeader().qosLevel() == MqttQoS.AT_MOST_ONCE) {
            byte[] messageBytes = new byte[msg.payload().readableBytes()];
            msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
            InternalMessage internalMessage = new InternalMessage()
                    .setTopic(msg.variableHeader().topicName())
                    .setMqttQoS(msg.fixedHeader().qosLevel().value())
                    .setMessageBytes(messageBytes)
                    .setDup(false)
                    .setRetain(false)
                    .setClientId(clientId);
            grozaKafkaService.send(internalMessage);
            this.sendPublishMessage(msg.variableHeader().topicName(), msg.fixedHeader().qosLevel(), messageBytes, false, false);
        }
        // QoS=1
        if (msg.fixedHeader().qosLevel() == MqttQoS.AT_LEAST_ONCE) {
            byte[] messageBytes = new byte[msg.payload().readableBytes()];
            msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
            InternalMessage internalMessage = new InternalMessage()
                    .setTopic(msg.variableHeader().topicName())
                    .setMqttQoS(msg.fixedHeader().qosLevel().value())
                    .setMessageBytes(messageBytes)
                    .setDup(false)
                    .setRetain(false)
                    .setClientId(clientId);
            grozaKafkaService.send(internalMessage);
            this.sendPublishMessage(msg.variableHeader().topicName(), msg.fixedHeader().qosLevel(), messageBytes, false, false);
            this.sendPubAckMessage(channel, msg.variableHeader().packetId());
        }
        // QoS=2
        if (msg.fixedHeader().qosLevel() == MqttQoS.EXACTLY_ONCE) {
            byte[] messageBytes = new byte[msg.payload().readableBytes()];
            msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
            InternalMessage internalMessage = new InternalMessage()
                    .setTopic(msg.variableHeader().topicName())
                    .setMqttQoS(msg.fixedHeader().qosLevel().value())
                    .setMessageBytes(messageBytes)
                    .setDup(false)
                    .setRetain(false)
                    .setClientId(clientId);
            grozaKafkaService.send(internalMessage);
            this.sendPublishMessage(msg.variableHeader().topicName(), msg.fixedHeader().qosLevel(), messageBytes, false, false);
            this.sendPubRecMessage(channel, msg.variableHeader().packetId());
        }
        // retain=1, 保留消息
        if (msg.fixedHeader().isRetain()) {
            byte[] messageBytes = new byte[msg.payload().readableBytes()];
            msg.payload().getBytes(msg.payload().readerIndex(), messageBytes);
            if (messageBytes.length == 0) {
                grozaRetainMessageStoreService.remove(msg.variableHeader().topicName());
            } else {
                RetainMessageStore retainMessageStore = new RetainMessageStore().setTopic(msg.variableHeader().topicName()).setMqttQoS(msg.fixedHeader().qosLevel().value())
                        .setMessageBytes(messageBytes);
                grozaRetainMessageStoreService.put(msg.variableHeader().topicName(), retainMessageStore);
            }
        }
    }

    private void sendPublishMessage(String topic, MqttQoS mqttQoS, byte[] messageBytes, boolean retain, boolean dup) {
        List<SubscribeStore> subscribeStores = grozaSubscribeStoreService.search(topic);
        subscribeStores.forEach(subscribeStore -> {
            if (grozaSessionStoreService.containsKey(subscribeStore.getClientId())) {
                // 订阅者收到MQTT消息的QoS级别, 最终取决于发布消息的QoS和主题订阅的QoS
                MqttQoS respQoS = mqttQoS.value() > subscribeStore.getMqttQoS() ? MqttQoS.valueOf(subscribeStore.getMqttQoS()) : mqttQoS;
                if (respQoS == MqttQoS.AT_MOST_ONCE) {
                    MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                            new MqttFixedHeader(MqttMessageType.PUBLISH, dup, respQoS, retain, 0),
                            new MqttPublishVariableHeader(topic, 0),
                            Unpooled.buffer().writeBytes(messageBytes));
                    log.info("PUBLISH - clientId: {}, topic: {}, Qos: {}", subscribeStore.getClientId(), topic, respQoS.value());
                    grozaSessionStoreService.get(subscribeStore.getClientId()).getChannel().writeAndFlush(publishMessage);
                }
                if (respQoS == MqttQoS.AT_LEAST_ONCE) {
                    int messageId = grozaMessageIdService.getNextMessageId();
                    MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                            new MqttFixedHeader(MqttMessageType.PUBLISH, dup, respQoS, retain, 0),
                            new MqttPublishVariableHeader(topic, messageId), Unpooled.buffer().writeBytes(messageBytes));
                    log.info("PUBLISH - clientId: {}, topic: {}, Qos: {}, messageId: {}", subscribeStore.getClientId(), topic, respQoS.value(), messageId);
                    DupPublishMessageStore dupPublishMessageStore = new DupPublishMessageStore().setClientId(subscribeStore.getClientId())
                            .setTopic(topic).setMqttQoS(respQoS.value()).setMessageBytes(messageBytes).setMessageId(messageId);
                    grozaDupPublishMessageStoreService.put(subscribeStore.getClientId(), dupPublishMessageStore);
                    grozaSessionStoreService.get(subscribeStore.getClientId()).getChannel().writeAndFlush(publishMessage);
                }
                if (respQoS == MqttQoS.EXACTLY_ONCE) {
                    int messageId = grozaMessageIdService.getNextMessageId();
                    MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                            new MqttFixedHeader(MqttMessageType.PUBLISH, dup, respQoS, retain, 0),
                            new MqttPublishVariableHeader(topic, messageId), Unpooled.buffer().writeBytes(messageBytes));
                    log.info("PUBLISH - clientId: {}, topic: {}, Qos: {}, messageId: {}", subscribeStore.getClientId(), topic, respQoS.value(), messageId);
                    DupPublishMessageStore dupPublishMessageStore = new DupPublishMessageStore().setClientId(subscribeStore.getClientId())
                            .setTopic(topic).setMqttQoS(respQoS.value()).setMessageBytes(messageBytes).setMessageId(messageId);
                    grozaDupPublishMessageStoreService.put(subscribeStore.getClientId(), dupPublishMessageStore);
                    grozaSessionStoreService.get(subscribeStore.getClientId()).getChannel().writeAndFlush(publishMessage);
                }
            }
        });
    }

    private void sendPubAckMessage(Channel channel, int messageId) {
        MqttPubAckMessage pubAckMessage = (MqttPubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId),
                null);
        channel.writeAndFlush(pubAckMessage);
    }

    private void sendPubRecMessage(Channel channel, int messageId) {
        MqttMessage pubRecMessage = MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBREC, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(messageId),
                null);
        channel.writeAndFlush(pubRecMessage);
    }

}
