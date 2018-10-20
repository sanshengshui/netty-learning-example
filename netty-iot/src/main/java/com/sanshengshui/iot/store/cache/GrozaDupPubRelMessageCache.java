package com.sanshengshui.iot.store.cache;

import com.sanshengshui.iot.common.message.DupPubRelMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author james
 * @date 2018年10月20日13:56
 */
@Service
public class GrozaDupPubRelMessageCache {
    public static final String CACHE_PRE = "groza:pubrel:";

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;


    public DupPubRelMessageStore put(String clientId, Integer messageId, DupPubRelMessageStore dupPubRelMessageStore){
        redisCacheTemplate.opsForHash().put(CACHE_PRE + clientId,String.valueOf(messageId),dupPubRelMessageStore);
        return dupPubRelMessageStore;
    }

    public ConcurrentHashMap<Integer,DupPubRelMessageStore> get(String clientId){
        ConcurrentHashMap<Integer, DupPubRelMessageStore> map = new ConcurrentHashMap<>();
        Map<Object,Object> map1 = redisCacheTemplate.opsForHash().entries(CACHE_PRE + clientId);
        if (map1 != null && !map1.isEmpty()) {
            map1.forEach((k, v) -> {
                map.put((Integer)k, (DupPubRelMessageStore)v);
            });
        }
        return map;
    }
    public boolean containsKey(String clientId){
        return redisCacheTemplate.hasKey(CACHE_PRE + clientId);
    }

    public void remove(String clientId,Integer messageId){
        redisCacheTemplate.opsForHash().delete(CACHE_PRE + clientId,messageId);
    }
    public void remove(String clientId){
        redisCacheTemplate.delete(CACHE_PRE + clientId);
    }
}
