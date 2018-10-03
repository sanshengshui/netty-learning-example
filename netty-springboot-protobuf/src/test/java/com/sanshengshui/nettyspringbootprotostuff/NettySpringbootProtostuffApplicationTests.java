package com.sanshengshui.nettyspringbootprotostuff;

import com.sanshengshui.netty.protobuf.UserMsg;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RunWith(JUnit4.class)
@Slf4j
public class NettySpringbootProtostuffApplicationTests {
    @Test
    public void ProtobufTest() throws IOException {
        UserMsg.User.Builder userInfo = UserMsg.User.newBuilder();
        userInfo.setId(1);
        userInfo.setName("mushuwei");
        userInfo.setName("24");
        UserMsg.User user = userInfo.build();
        // 将数据写到输出流
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        user.writeTo(output);
        // 将数据序列化后发送
        byte[] byteArray = output.toByteArray();
        // 接收到流并读取
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        // 反序列化
        UserMsg.User userInfo2 = UserMsg.User.parseFrom(input);
        log.info("id:" + userInfo2.getId());
        log.info("name:" + userInfo2.getName());
        log.info("age:" + userInfo2.getAge());

    }
}
