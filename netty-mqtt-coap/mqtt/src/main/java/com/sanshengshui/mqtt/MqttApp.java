package com.sanshengshui.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 穆书伟
 * @date 2018年9月24日
 */
@ComponentScan({"com.sanshengshui.mqtt"})
@SpringBootConfiguration
public class MqttApp {
    public static void main(String[] args){
        SpringApplication.run(MqttApp.class,args);
    }
}
