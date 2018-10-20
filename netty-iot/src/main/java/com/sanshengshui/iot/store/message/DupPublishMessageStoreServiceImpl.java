package com.sanshengshui.iot.store.message;

import com.sanshengshui.iot.common.message.DupPublishMessageStore;
import com.sanshengshui.iot.common.message.GrozaDupPublishMessageStoreService;
import com.sanshengshui.iot.store.cache.GrozaDupPublishMessageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DupPublishMessageStoreServiceImpl implements GrozaDupPublishMessageStoreService {

    @Autowired
    private GrozaDupPublishMessageCache grozaDupPublishMessageCache;


    @Override
    public void put(String clientId, DupPublishMessageStore dupPublishMessageStore) {
        grozaDupPublishMessageCache.put(clientId,dupPublishMessageStore.getMessageId(),dupPublishMessageStore);
    }

    @Override
    public List<DupPublishMessageStore> get(String clientId) {
        if (grozaDupPublishMessageCache.containsKey(clientId)){
            ConcurrentHashMap<Integer,DupPublishMessageStore> map = grozaDupPublishMessageCache.get(clientId);
            Collection<DupPublishMessageStore> collection = map.values();
            return new ArrayList<>(collection);
        }
        return new ArrayList<>();
    }

    @Override
    public void remove(String clientId, int messageId) {
        grozaDupPublishMessageCache.remove(clientId,messageId);
    }

    @Override
    public void removeByClient(String clientId) {
        grozaDupPublishMessageCache.remove(clientId);
    }
}
