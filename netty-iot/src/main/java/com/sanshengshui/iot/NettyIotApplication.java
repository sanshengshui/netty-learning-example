package com.sanshengshui.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sanshengshui.iot"})
public class NettyIotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyIotApplication.class, args);
    }
}
