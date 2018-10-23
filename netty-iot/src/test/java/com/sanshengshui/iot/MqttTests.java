package com.sanshengshui.iot;

import com.sanshengshui.iot.common.message.DupPublishMessageStore;
import com.sanshengshui.iot.common.session.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MqttTests {
    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    public static final String CACHE_SES = "groza:session:";
    public static final String CACHE_DUPPub = "groza:duppub";

    /**
     * 会话存储测试
     */
//    @Test
//    public void GrozaSessionStoreTest(){
//
//        SessionStore sessionStore = new SessionStore();
//        sessionStore.setChannelId("20181018163127");
//        sessionStore.setCleanSession(true);
//        sessionStore.setClientId("111");
//        redisCacheTemplate.opsForValue().set(CACHE_SES + "20181018163127",sessionStore);
//        final SessionStore sessionStore1 = (SessionStore) redisCacheTemplate.opsForValue().get(CACHE_SES + "20181018163127");
//        log.info("[对象缓存结果] - [{}]", sessionStore1.toString());
//
//    }

    @Test
    public void GrozaDupPublishMessageStoreTest(){
        DupPublishMessageStore dupPublishMessageStore = new DupPublishMessageStore();
        dupPublishMessageStore.setClientId("20181020112852");
        dupPublishMessageStore.setMessageId(123456);
        dupPublishMessageStore.setMqttQoS(1);
        dupPublishMessageStore.setTopic("test");
        dupPublishMessageStore.setMessageBytes(new byte[]{1,2,3,4,5,6});
        redisCacheTemplate.opsForHash().put(CACHE_DUPPub + "20181020112852",123456,dupPublishMessageStore);
        redisCacheTemplate.opsForHash().put(CACHE_DUPPub + "20181020112852",123457,dupPublishMessageStore);
        redisCacheTemplate.hasKey(CACHE_DUPPub + "20181020112852");
        log.info(redisCacheTemplate.hasKey(CACHE_DUPPub + "20181020112852").toString());
        Map<Object,Object> map1 = redisCacheTemplate.opsForHash().entries(CACHE_DUPPub + "20181020112852");
        if (map1 != null && !map1.isEmpty()) {
            map1.forEach((k, v) -> {
                log.info(k.toString());
                 DupPublishMessageStore dupPublishMessageStore1 = (DupPublishMessageStore) v;
                 log.info(dupPublishMessageStore1.getTopic());
            });
        }
        redisCacheTemplate.opsForHash().delete(CACHE_DUPPub + "20181020112852",123456);
        redisCacheTemplate.delete(CACHE_DUPPub + "20181020112852");

    }
}
