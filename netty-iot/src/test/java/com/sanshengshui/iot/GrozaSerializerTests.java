package com.sanshengshui.iot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrozaSerializerTests {
    private static final Logger log = LoggerFactory.getLogger(GrozaSerializerTests.class);

    @Test
    public void serializeTests(){
        User user = new User();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        user.setId(1l);
        user.setPassword("123");
        user.setUsername("mushuwei");
        log.info(gson.toJson(user));
        String s = new String(gson.toJson(user).getBytes());
        Gson gson1 = new Gson();
        log.info(gson.fromJson(s, User.class).getUsername());
    }

}
