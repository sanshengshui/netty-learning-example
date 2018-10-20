package com.sanshengshui.iot.store.message;

import com.sanshengshui.iot.common.message.DupPubRelMessageStore;
import com.sanshengshui.iot.common.message.GrozaDupPubRelMessageStoreService;
import com.sanshengshui.iot.store.cache.GrozaDupPubRelMessageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DupPubRelMessageStoreServiceImpl implements GrozaDupPubRelMessageStoreService {
    @Autowired
    private GrozaDupPubRelMessageCache grozaDupPubRelMessageCache;

    @Override
    public void put(String clientId, DupPubRelMessageStore dupPubRelMessageStore) {

    }

    @Override
    public List<DupPubRelMessageStore> get(String clientId) {
        return null;
    }

    @Override
    public void remove(String clientId, int messageId) {

    }

    @Override
    public void removeByClient(String clientId) {

    }
}
