package com.sanshengshui.iot;

import com.sanshengshui.iot.common.session.SessionStore;
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

        SessionStore sessionStore = new SessionStore();
        sessionStore.setChannelId("20181018163127");
        sessionStore.setCleanSession(true);
        sessionStore.setClientId("111");
        redisCacheTemplate.opsForValue().set("graza:20181018163127",sessionStore);
        final SessionStore sessionStore1 = (SessionStore) redisCacheTemplate.opsForValue().get("graza:20181018163127");
        log.info("[对象缓存结果] - [{}]", sessionStore1);
        log.info(sessionStore.toString());

    }
}
