package com.sanshengshui.persistence.controller;

import com.sanshengshui.persistence.device.DeviceService;
import com.sanshengshui.persistence.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/device", method = RequestMethod.POST)
    @ResponseBody
    public Device saveDevice(@RequestBody Device device) {
        return  deviceService.saveDevice(device);
    }
}
