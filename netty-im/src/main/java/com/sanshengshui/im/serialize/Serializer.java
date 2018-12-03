package com.sanshengshui.im.serialize;

import com.sanshengshui.im.serialize.impl.JSONSerializer;

/**
 * @author james mu
 * @date 18-12-3 下午3:24
 */
public interface Serializer {
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
