package com.sanshengshui.iot.common.session;


import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.io.Serializable;

/**
 * 回话存储
 * @author 穆书伟
 */
public class SessionStore implements Serializable {
    private static final long serialVersionUID = 5209539791996944490L;

    private String clientId;

    private Channel channel;

    private boolean cleanSession;

    private MqttPublishMessage willMessage;

    public SessionStore(String clientId, Channel channel, boolean cleanSession, MqttPublishMessage willMessage) {
        this.clientId = clientId;
        this.channel = channel;
        this.cleanSession = cleanSession;
        this.willMessage = willMessage;
    }

    public String getClientId() {
        return clientId;
    }

    public SessionStore setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

    public SessionStore setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public SessionStore setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
        return this;
    }

    public MqttPublishMessage getWillMessage() {
        return willMessage;
    }

    public SessionStore setWillMessage(MqttPublishMessage willMessage) {
        this.willMessage = willMessage;
        return this;
    }
}
