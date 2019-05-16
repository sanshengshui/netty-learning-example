package com.sanshengshui.persistence.sql;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.sanshengshui.persistence.Dao;
import com.sanshengshui.persistence.DaoUtil;
import com.sanshengshui.persistence.entity.BaseEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author james mu
 * @date 2019/5/15 1:44
 */
public abstract class JpaAbstractDao<E extends BaseEntity<D>,D>
        extends JpaAbstractDaoListeningExecutorService
        implements Dao<D> {

    protected abstract Class<E> getEntityClass();

    protected abstract CrudRepository<E,Long> getCrudRepository();

    @Override
    public List<D> find() {
        List<E> entities = Lists.newArrayList(getCrudRepository().findAll());
        return DaoUtil.convertDataList(entities);
    }

    @Override
    public D findById(Long id) {
        E entity = getCrudRepository().findById(id).get();
        return DaoUtil.getData(entity);
    }

    @Override
    public ListenableFuture<D> findByIdAsync(Long id) {
        return service.submit(() -> DaoUtil.getData(getCrudRepository().findById(id).get()));
    }

    @Override
    @Transactional
    public D save(D domain) {
        E entity;
        try {
            entity = getEntityClass().getConstructor(domain.getClass()).newInstance(domain);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't create entity for domain object {" + domain + "}", e);
        }
        entity = getCrudRepository().save(entity);
        return  DaoUtil.getData(entity);
    }

    @Override
    public boolean removeById(Long id) {
        getCrudRepository().deleteById(id);
        return getCrudRepository().findById(id).isPresent();
    }
}
