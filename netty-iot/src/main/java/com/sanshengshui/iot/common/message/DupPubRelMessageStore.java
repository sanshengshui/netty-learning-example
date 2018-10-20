package com.sanshengshui.iot.common.message;

import java.io.Serializable;

/**
 * @author james
 * PUBREL重发消息存储
 */
public class DupPubRelMessageStore implements Serializable {

    private static final long serialVersionUID = -4111642532532950980L;

    private String clientId;

    private int messageId;

    public DupPubRelMessageStore(){

    }

    public String getClientId() {
        return this.clientId;
    }

    public DupPubRelMessageStore setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public DupPubRelMessageStore setMessageId(int messageId) {
        this.messageId = messageId;
        return this;
    }
}
