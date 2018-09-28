package com.sanshengshui.kafka.kafka;

import com.thinkerou.proto.helloworld.Callback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {

    @Autowired
    private KafkaTemplate<Long, Callback> kafkaTemplate;

    public void send(Callback request) {
        kafkaTemplate.send("spring-kafka-protobuf-demo", request);
    }
}
