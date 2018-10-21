package com.sanshengshui.iot.store.message;

import com.sanshengshui.iot.common.message.GrozaMessageIdService;
import org.springframework.stereotype.Service;

@Service
public class MessageIdServiceImpl implements GrozaMessageIdService {
    @Override
    public int getNextMessageId() {
        return 0;
    }
}
