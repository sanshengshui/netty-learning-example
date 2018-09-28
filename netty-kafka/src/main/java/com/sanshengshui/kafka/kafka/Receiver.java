package com.sanshengshui.kafka.kafka;

import com.thinkerou.proto.helloworld.Callback;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {
    @KafkaListener(topics = {"spring-kafka-protobuf-demo"})
    public void listen(ConsumerRecord<?, Callback> record) {
        Callback callback = record.value();
        log.info("message: {}", callback);
        if (callback == null){
            return;
        }

        switch (callback.getEventTypeCase()) {
            case ONE_EVENT:
                log.info("one event: {}", callback.getOneEvent());
                break;
            case TWO_EVENT:
                log.info("two event: {}", callback.getOneEvent());
                break;
            default:
                log.info("default value...");
                break;
        }
    }
}
