package com.sanshengshui.iot.store.subscribe;

import cn.hutool.core.util.StrUtil;
import com.sanshengshui.iot.common.subscribe.GrozaSubscribeStoreService;
import com.sanshengshui.iot.common.subscribe.SubscribeStore;
import com.sanshengshui.iot.store.cache.GrozaSubscribeNotWildcardCache;
import com.sanshengshui.iot.store.cache.GrozaSubscribeWildcardCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
        if (StrUtil.contains(topicFilter,'#') || StrUtil.contains(topicFilter,'+')){
            grozaSubscribeWildcardCache.put(topicFilter,subscribeStore.getClientId(),subscribeStore);
        }else{
            grozaSubscribeNotWildcardCache.put(topicFilter,subscribeStore.getClientId(),subscribeStore);
        }
    }

    @Override
    public void remove(String topicFilter, String clientId) {
        if (StrUtil.contains(topicFilter,'#') || StrUtil.contains(topicFilter,'+')){
            grozaSubscribeWildcardCache.remove(topicFilter,clientId);
        }else {
            grozaSubscribeNotWildcardCache.remove(topicFilter,clientId);
        }
    }

    @Override
    public void removeForClient(String clientId) {
        grozaSubscribeNotWildcardCache.removeForClient(clientId);
        grozaSubscribeWildcardCache.removeForClient(clientId);
    }

    @Override
    public List<SubscribeStore> search(String topic) {
        List<SubscribeStore> subscribeStores = new ArrayList<SubscribeStore>();
        List<SubscribeStore> list = grozaSubscribeNotWildcardCache.all(topic);
        if (list.size() > 0) {
            subscribeStores.addAll(list);
        }
        grozaSubscribeWildcardCache.all().forEach((topicFilter, map) -> {
            if (StrUtil.split(topic, '/').size() >= StrUtil.split(topicFilter, '/').size()) {
                List<String> splitTopics = StrUtil.split(topic, '/');//a
                List<String> spliteTopicFilters = StrUtil.split(topicFilter, '/');//#
                String newTopicFilter = "";
                for (int i = 0; i < spliteTopicFilters.size(); i++) {
                    String value = spliteTopicFilters.get(i);
                    if (value.equals("+")) {
                        newTopicFilter = newTopicFilter + "+/";
                    } else if (value.equals("#")) {
                        newTopicFilter = newTopicFilter + "#/";
                        break;
                    } else {
                        newTopicFilter = newTopicFilter + splitTopics.get(i) + "/";
                    }
                }
                newTopicFilter = StrUtil.removeSuffix(newTopicFilter, "/");
                if (topicFilter.equals(newTopicFilter)) {
                    Collection<SubscribeStore> collection = map.values();
                    List<SubscribeStore> list2 = new ArrayList<SubscribeStore>(collection);
                    subscribeStores.addAll(list2);
                }
            }
        });
        return subscribeStores;
    }
}
