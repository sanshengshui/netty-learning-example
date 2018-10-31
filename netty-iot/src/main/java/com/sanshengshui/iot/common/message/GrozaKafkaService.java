package com.sanshengshui.iot.common.message;

import com.sanshengshui.iot.internal.InternalMessage;

/**
 * 消息转发,基于kafka
 * @author james
 */
public interface GrozaKafkaService {
    void send(InternalMessage internalMessage);
}
