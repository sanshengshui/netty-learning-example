package com.sanshengshui.im.protocol.response;

import com.sanshengshui.im.protocol.Packet;
import lombok.Data;


import static com.sanshengshui.im.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {
    private String userId;

    private String userName;

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {

        return LOGIN_RESPONSE;
    }
}
