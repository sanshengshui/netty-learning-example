package com.sanshengshui.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.sanshengshui.netty.client"})
public class NettyClientApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(NettyClientApp.class);
    }
}
