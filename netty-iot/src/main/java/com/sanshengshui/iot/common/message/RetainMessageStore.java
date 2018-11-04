/**
 * Copyright (c) 2018, Mr.Wang (recallcode@aliyun.com) All rights reserved.
 */

package com.sanshengshui.iot.common.message;

import java.io.Serializable;

/**
 * @author james
 * Retain标志消息存储
 */
public class RetainMessageStore implements Serializable {

	private static final long serialVersionUID = -7548204047370972779L;

	private String topic;

	private byte[] messageBytes;

	private int mqttQoS;

	public String getTopic() {
		return topic;
	}

	public RetainMessageStore setTopic(String topic) {
		this.topic = topic;
		return this;
	}

	public byte[] getMessageBytes() {
		return messageBytes;
	}

	public RetainMessageStore setMessageBytes(byte[] messageBytes) {
		this.messageBytes = messageBytes;
		return this;
	}

	public int getMqttQoS() {
		return mqttQoS;
	}

	public RetainMessageStore setMqttQoS(int mqttQoS) {
		this.mqttQoS = mqttQoS;
		return this;
	}
}
