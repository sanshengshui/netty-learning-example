package com.sanshengshui.persistence.sql.Device;

import com.sanshengshui.persistence.entity.sql.DeviceEntity;
import com.sanshengshui.persistence.util.SqlDao;
import org.springframework.data.repository.CrudRepository;

@SqlDao
public interface DeviceRepository extends CrudRepository<DeviceEntity,Long> {

}
