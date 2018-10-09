package com.sanshengshui.netty.serialize;

/**
 * 序列化接口类
 */
public interface Serializer {
    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
