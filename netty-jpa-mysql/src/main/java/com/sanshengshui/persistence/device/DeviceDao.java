package com.sanshengshui.persistence.device;

import com.google.common.util.concurrent.ListenableFuture;
import com.sanshengshui.persistence.Dao;
import com.sanshengshui.persistence.domain.Device;

import java.util.List;

/**
 * @author james mu
 * @date 18-12-25 下午4:41
 */
public interface DeviceDao extends Dao<Device> {

    /**
     * Save or update device object
     *
     * @param device the device object
     * @return saved device object
     */
    Device save(Device device);

    /**
     * Find devices
     *
     * @return the list of device objects
     */
    @Override
    List<Device> find();

    /**
     * Find device by device id
     *
     * @param id
     * @return
     */
    @Override
    Device findById(Long id);

    @Override
    ListenableFuture<Device> findByIdAsync(Long id);

    @Override
    boolean removeById(Long id);

}
