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

    public SessionStore(String clientId, String channelId, boolean cleanSession) {
        this.clientId = clientId;
        this.channelId = channelId;
        this.cleanSession = cleanSession;
    }
    public SessionStore(){

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }
}
