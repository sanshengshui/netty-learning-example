package com.sanshengshui.iot.store.cache;

import com.sanshengshui.iot.common.subscribe.SubscribeStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author james
 * @date 2018年10月20日17:59
 */
@Service
public class GrozaSubscribeNotWildcardCache {
    public static final String CACHE_PRE = "groza:subnotwildcard:";
    public static final String CACHE_CLIENT_PRE = "groza:client:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    public SubscribeStore put(String topic,String clientId,SubscribeStore subscribeStore){
        redisCacheTemplate.opsForHash().put(CACHE_PRE + topic,clientId,subscribeStore);
        stringRedisTemplate.opsForSet().add(CACHE_CLIENT_PRE + clientId,topic);
        return subscribeStore;
    }
    public SubscribeStore get(String topic,String clientId){
        return (SubscribeStore) redisCacheTemplate.opsForHash().get(CACHE_PRE + topic,clientId);
    }
    public boolean containsKey(String topic,String clientId){
        return redisCacheTemplate.opsForHash().hasKey(CACHE_PRE + topic,clientId);
    }
    public void remove(String topic,String clientId){
        stringRedisTemplate.opsForSet().remove(CACHE_CLIENT_PRE + clientId,topic);
        redisCacheTemplate.opsForHash().delete(CACHE_PRE + topic,clientId);
    }
    public void removeForClient(String clientId){
        for (String topic : stringRedisTemplate.opsForSet().members(CACHE_CLIENT_PRE + clientId)){
         redisCacheTemplate.opsForHash().delete(CACHE_PRE + topic,clientId);
        }
        stringRedisTemplate.delete(CACHE_CLIENT_PRE + clientId);
    }
    public Map<String, ConcurrentHashMap<String, SubscribeStore>> all(){
        Map<String, ConcurrentHashMap<String, SubscribeStore>> map = new HashMap<>();
        Set<String> set = redisCacheTemplate.keys(CACHE_PRE + "*");
        if (set != null && !set.isEmpty()) {
            set.forEach(
                    entry -> {
                        ConcurrentHashMap<String, SubscribeStore> map1 = new ConcurrentHashMap<>();
                        Map<Object, Object> map2 = redisCacheTemplate.opsForHash().entries(entry);
                        if (map2 != null && !map2.isEmpty()) {
                            map2.forEach((k, v) -> {
                                map1.put((String)k, (SubscribeStore)v);
                            });
                            map.put(entry.substring(CACHE_PRE.length()), map1);
                        }
                    }
            );
        }
        return map;
    }
    public List<SubscribeStore> all(String topic){
        List<SubscribeStore> list = new ArrayList<>();
        Map<Object,Object> map = redisCacheTemplate.opsForHash().entries(CACHE_PRE + topic);
        if (map != null && !map.isEmpty()) {
            map.forEach((k, v) -> {
                list.add((SubscribeStore) v);
            });
        }
        return list;
    }
}
