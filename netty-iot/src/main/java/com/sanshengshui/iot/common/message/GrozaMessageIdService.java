package com.sanshengshui.iot.common.message;
/**
 * @author james
 * 分布式生成报文标识符
 */
public interface GrozaMessageIdService {
    /**
     * 获取报文标识符
     */
    int getNextMessageId();
}
