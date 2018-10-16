package com.sanshengshui.iot.common.message;

import java.io.Serializable;

/**
 * @author james
 * @description PUBLISH重发消息存储
 */
public class DupPublishMessageStore implements Serializable {
    private static final long serialVersionUID = -8112511377194421600L;
    private String clientId;
    private String topic;
    private int mqttQoS;
    private int messageId;
    private byte[] messageBytes;

    public DupPublishMessageStore() {
    }

    public String getClientId() {
        return this.clientId;
    }

    public DupPublishMessageStore setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTopic() {
        return this.topic;
    }

    public DupPublishMessageStore setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getMqttQoS() {
        return this.mqttQoS;
    }

    public DupPublishMessageStore setMqttQoS(int mqttQoS) {
        this.mqttQoS = mqttQoS;
        return this;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public DupPublishMessageStore setMessageId(int messageId) {
        this.messageId = messageId;
        return this;
    }

    public byte[] getMessageBytes() {
        return this.messageBytes;
    }

    public DupPublishMessageStore setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
        return this;
    }
}
