package com.sanshengshui.persistence.device;

import com.sanshengshui.persistence.Dao;
import com.sanshengshui.persistence.domain.Device;


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

}
