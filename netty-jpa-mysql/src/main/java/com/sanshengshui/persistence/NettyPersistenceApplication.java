package com.sanshengshui.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author james mu
 * @date 18-12-25 下午3:30
 */
@SpringBootApplication(scanBasePackages = {"com.sanshengshui.persistence"})
public class NettyPersistenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyPersistenceApplication.class);
    }
}
