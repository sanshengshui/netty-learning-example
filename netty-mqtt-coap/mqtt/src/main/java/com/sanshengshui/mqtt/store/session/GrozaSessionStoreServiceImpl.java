package com.sanshengshui.mqtt.store.session;

import com.sanshengshui.mqtt.common.session.GrozaSessionStoreService;
import com.sanshengshui.mqtt.common.session.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GrozaSessionStoreServiceImpl implements GrozaSessionStoreService {
    public static final String CACHE_PRE = "groza:";

    @Override
    public void put(String clientId, SessionStore sessionStore) {

    }

    @Override
    public SessionStore get(String clientId) {
        return null;
    }

    @Override
    public boolean containsKey(String clientId) {
        return false;
    }

    @Override
    public void remove(String clientId) {

    }
}
