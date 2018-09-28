package com.sanshengshui.kafka.service;

import com.sanshengshui.kafka.NettyKafkaApplication;
import com.sanshengshui.kafka.kafka.Sender;
import com.thinkerou.proto.helloworld.Callback;
import com.thinkerou.proto.helloworld.KafkaEventOne;
import com.thinkerou.proto.helloworld.KafkaEventTwo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService{
    public Map<String, Object> run() {
        Map<String, Object> result = new HashMap<>();

        Callback request = Callback.newBuilder()
                .setOneEvent(KafkaEventOne.newBuilder().setAddress("beijing").build())
                .setTwoEvent(KafkaEventTwo.newBuilder().setNumber(123456).build())
                .build();

        Sender sender = NettyKafkaApplication.context.getBean(Sender.class);
        sender.send(request);

        return result;
    }
}
