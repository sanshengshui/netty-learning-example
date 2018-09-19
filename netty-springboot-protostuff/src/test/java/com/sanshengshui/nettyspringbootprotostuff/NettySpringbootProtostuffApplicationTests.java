package com.sanshengshui.nettyspringbootprotostuff;

import com.sanshengshui.netty.protostuff.ProtoStuffSerializerUtil;
import com.sanshengshui.netty.protostuff.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(JUnit4.class)
public class NettySpringbootProtostuffApplicationTests {

    @Test
    public void protostuffTests(){
        User user = new User();
        user.setId(1);
        user.setName("sanshengshui");
        byte[] arr = ProtoStuffSerializerUtil.serialize(user);
        User result = ProtoStuffSerializerUtil.deserialize(arr,User.class);
        System.out.println(result.getId());

    }

}
