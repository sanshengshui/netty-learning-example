package com.sanshengshui.netty.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.sanshengshui.netty.serialize.Serializer;

/**
 * @author 穆书伟
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
