package com.sanshengshui.persistence.device;

import com.sanshengshui.persistence.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public Device saveDevice(Device device) {
        Device savedDevice = deviceDao.save(device);
        return savedDevice;
    }
}
