package com.sanshengshui.netty;

import com.sanshengshui.netty.client.NettyClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NettyClientApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context = SpringApplication.run(NettyClientApp.class, args);
        NettyClient nettyClient = context.getBean(NettyClient.class);
        nettyClient.run();
    }
}
