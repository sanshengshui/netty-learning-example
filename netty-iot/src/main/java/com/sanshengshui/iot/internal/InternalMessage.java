package com.sanshengshui.iot.internal;

import java.io.Serializable;

/**
 * @author james
 * @description 内部消息
 */
public class InternalMessage implements Serializable {
    public static final long serialVersionUID = -1L;

    private String clientId;

    private String topic;

    private int mqttQoS;

    private byte[] messageBytes;

    private boolean retain;

    private boolean dup;

    public String getClientId() {
        return clientId;
    }

    public InternalMessage setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public InternalMessage setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getMqttQoS() {
        return mqttQoS;
    }

    public InternalMessage setMqttQoS(int mqttQoS) {
        this.mqttQoS = mqttQoS;
        return this;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public InternalMessage setMessageBytes(byte[] messageBytes) {
        this.messageBytes = messageBytes;
        return this;
    }

    public boolean isRetain() {
        return retain;
    }

    public InternalMessage setRetain(boolean retain) {
        this.retain = retain;
        return this;
    }

    public boolean isDup() {
        return dup;
    }

    public InternalMessage setDup(boolean dup) {
        this.dup = dup;
        return this;
    }
}
