package com.sanshengshui.iot.store.session;

import com.sanshengshui.iot.common.session.GrozaSessionStoreService;
import com.sanshengshui.iot.common.session.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class GrozaSessionStoreServiceImpl implements GrozaSessionStoreService {

    public static final String CACHE_PRE = "groza:";

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;


    @Override
    public void put(String clientId, SessionStore sessionStore) {
        redisCacheTemplate.opsForValue().set(CACHE_PRE + clientId, sessionStore);
    }

    @Override
    public SessionStore get(String clientId) {
        SessionStore obj = (SessionStore) redisCacheTemplate.opsForValue().get(CACHE_PRE + clientId);
        return obj;
    }

    @Override
    public boolean containsKey(String clientId) {
        return redisCacheTemplate.hasKey(CACHE_PRE + clientId);
    }

    @Override
    public void remove(String clientId) {
        redisCacheTemplate.delete(CACHE_PRE + clientId);

    }
}
