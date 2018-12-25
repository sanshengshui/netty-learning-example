package com.sanshengshui.persistence;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * @author james mu
 * @date 18-12-25 下午4:10
 */
public interface Dao<T> {

    List<T> find();

    T findById(Long id);

    ListenableFuture<T> findByIdAsync(Long id);

    T save(T t);

    boolean removeById(Long id);

}
