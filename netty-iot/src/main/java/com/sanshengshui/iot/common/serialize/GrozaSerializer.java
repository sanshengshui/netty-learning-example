package com.sanshengshui.iot.common.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanshengshui.iot.common.session.SessionStore;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author james
 * 继承RedisTemplate序列化接口,自定义序列化类
 */
public class GrozaSerializer implements RedisSerializer<Object> {
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(o).getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
       String s = new String(bytes);
       Gson gson = new Gson();
       return gson.fromJson(s, SessionStore.class);
    }
}
