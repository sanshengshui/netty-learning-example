package com.sanshengshui.nosql.dao;

import com.sanshengshui.nosql.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, Long> {
}
