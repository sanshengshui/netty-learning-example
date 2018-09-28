package com.sanshengshui.kafka.controller;

import com.sanshengshui.kafka.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/n/spring/kafka/protobuf")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("/demo")
    public Object demo() {
        Map<String, Object> result = demoService.run();
        return result;
    }
}
