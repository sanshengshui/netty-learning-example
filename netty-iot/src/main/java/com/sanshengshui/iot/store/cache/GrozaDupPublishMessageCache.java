package com.sanshengshui.iot.store.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class GrozaDupPublishMessageCache {
    private final static String CACHE_PRE = "mqtt:publish:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
}
