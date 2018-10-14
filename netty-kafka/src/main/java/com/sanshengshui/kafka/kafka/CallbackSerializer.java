package com.sanshengshui.kafka.kafka;

import com.thinkerou.proto.helloworld.Callback;
import org.apache.kafka.common.serialization.Serializer;


public class CallbackSerializer extends Adapter implements Serializer<Callback> {
    @Override
    public byte[] serialize(final String topic, final Callback data) {
        return data.toByteArray();
    }
}

