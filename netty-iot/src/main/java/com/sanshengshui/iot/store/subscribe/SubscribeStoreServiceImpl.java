package com.sanshengshui.iot.store.subscribe;

import com.sanshengshui.iot.common.subscribe.GrozaSubscribeStoreService;
import com.sanshengshui.iot.common.subscribe.SubscribeStore;
import com.sanshengshui.iot.store.cache.GrozaSubscribeNotWildcardCache;
import com.sanshengshui.iot.store.cache.GrozaSubscribeWildcardCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订阅存储服务
 */
@Service
public class SubscribeStoreServiceImpl implements GrozaSubscribeStoreService {
    @Autowired
    private GrozaSubscribeWildcardCache grozaSubscribeWildcardCache;

    @Autowired
    private GrozaSubscribeNotWildcardCache grozaSubscribeNotWildcardCache;

    @Override
    public void put(String topicFilter, SubscribeStore subscribeStore) {

    }

    @Override
    public void remove(String topicFilter, String clientId) {

    }

    @Override
    public void removeForClient(String clientId) {

    }

    @Override
    public List<SubscribeStore> search(String topic) {
        return null;
    }
}
