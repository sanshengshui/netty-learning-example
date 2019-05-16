package com.sanshengshui.persistence.sql.Device;

import com.sanshengshui.persistence.device.DeviceDao;

import com.sanshengshui.persistence.domain.Device;
import com.sanshengshui.persistence.entity.sql.DeviceEntity;
import com.sanshengshui.persistence.sql.JpaAbstractDao;
import com.sanshengshui.persistence.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@SqlDao
public class JpaDeviceDao extends JpaAbstractDao<DeviceEntity, Device> implements DeviceDao {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    protected Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }

    @Override
    protected CrudRepository<DeviceEntity, Long> getCrudRepository() {
        return deviceRepository;
    }



}
