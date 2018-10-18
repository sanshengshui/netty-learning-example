package com.sanshengshui.iot;

import com.google.gson.Gson;
import com.sanshengshui.iot.common.session.SessionStore;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MqttTests {
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    public static final String CACHE_PRE = "groza:";

    @Test
    public void GrozaSessionStoreTest(){

        SessionStore sessionStore = new SessionStore("20181018163127","123456789",true,null);

        MqttPublishMessage willMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttPublishVariableHeader("/test", 0),
                Unpooled.buffer().writeBytes(new byte[]{1,2,3,4,5,6}));
        sessionStore.setWillMessage(willMessage);

        log.info(sessionStore.toString());

    }
}
