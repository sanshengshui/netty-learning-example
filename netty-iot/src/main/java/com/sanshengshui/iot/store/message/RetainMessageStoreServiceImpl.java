package com.sanshengshui.iot.store.message;

import cn.hutool.core.util.StrUtil;
import com.sanshengshui.iot.common.message.GrozaRetainMessageStoreService;
import com.sanshengshui.iot.common.message.RetainMessageStore;
import com.sanshengshui.iot.store.cache.GrozaRetainMessageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RetainMessageStoreServiceImpl implements GrozaRetainMessageStoreService {
    @Autowired
    private GrozaRetainMessageCache grozaRetainMessageCache;

    @Override
    public void put(String topic, RetainMessageStore retainMessageStore) {
        grozaRetainMessageCache.put(topic,retainMessageStore);
    }

    @Override
    public RetainMessageStore get(String topic) {
        return grozaRetainMessageCache.get(topic);
    }

    @Override
    public void remove(String topic) {
        grozaRetainMessageCache.remove(topic);
    }

    @Override
    public boolean containsKey(String topic) {
        return grozaRetainMessageCache.containsKey(topic);
    }

    @Override
    public List<RetainMessageStore> search(String topicFilter) {
        List<RetainMessageStore> retainMessageStores = new ArrayList<RetainMessageStore>();
        if (!StrUtil.contains(topicFilter, '#') && !StrUtil.contains(topicFilter, '+')) {
            if (grozaRetainMessageCache.containsKey(topicFilter)) {
                retainMessageStores.add(grozaRetainMessageCache.get(topicFilter));
            }
        } else {
            grozaRetainMessageCache.all().forEach((topic, val) -> {
                if (StrUtil.split(topic, '/').size() >= StrUtil.split(topicFilter, '/').size()) {
                    List<String> splitTopics = StrUtil.split(topic, '/');
                    List<String> spliteTopicFilters = StrUtil.split(topicFilter, '/');
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
                        retainMessageStores.add(val);
                    }
                }
            });
        }
        return retainMessageStores;
    }
}
