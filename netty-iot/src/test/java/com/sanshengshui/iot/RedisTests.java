package com.sanshengshui.iot;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTests {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void HyperLogLogTest(){
        for (int i = 0;i<100000;i++){
            redisTemplate.opsForHyperLogLog().add("codehole","user"+i);
            }
        }
    }

