package com.sanshengshui.iot.common.session;

import io.netty.handler.codec.mqtt.MqttPublishMessage;

import java.io.Serializable;

/**
 * 回话存储
 * @author 穆书伟
 */
public class SessionStore implements Serializable {
    private static final long serialVersionUID = -1L;

    private String clientId;

    private String channelId;

    private boolean cleanSession;

    private MqttPublishMessage willMessage;

    public SessionStore(String clientId, String channelId, boolean cleanSession, MqttPublishMessage willMessage) {
        this.clientId = clientId;
        this.channelId = channelId;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
