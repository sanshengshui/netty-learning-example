package com.sanshengshui.persistence;

import com.sanshengshui.persistence.entity.ToData;

import java.util.*;

public abstract class DaoUtil {

    private DaoUtil() {
    }

    public static <T> List<T> convertDataList(Collection<? extends ToData<T>> toDataList) {
        List<T> list = Collections.emptyList();
        if (toDataList != null && !toDataList.isEmpty()) {
            list = new ArrayList<>();
            for (ToData<T> object : toDataList) {
                if (object != null) {
                    list.add(object.toData());
                }
            }
        }
        return list;
    }

    public static <T> T getData(ToData<T> data) {
        T object = null;
        if (data != null) {
            object = data.toData();
        }
        return object;
    }

    public static Long getId(Long idBased) {
        Long id = null;
        if (idBased != null) {
            id = idBased;
        }
        return id;
    }

    public static List<Long> toUUIDs(List<? extends Long> idBasedIds) {
        List<Long> ids = new ArrayList<>();
        for (Long idBased : idBasedIds) {
            ids.add(getId(idBased));
        }
        return ids;
    }

}
