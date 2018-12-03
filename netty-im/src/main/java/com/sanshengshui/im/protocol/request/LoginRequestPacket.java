package com.sanshengshui.im.protocol.request;

import com.sanshengshui.im.protocol.Packet;
import lombok.Data;

import static com.sanshengshui.im.protocol.command.Command.LOGIN_REQUEST;


@Data
public class LoginRequestPacket extends Packet {
    private String userName;

    private String password;

    @Override
    public Byte getCommand() {

        return LOGIN_REQUEST;
    }
}
