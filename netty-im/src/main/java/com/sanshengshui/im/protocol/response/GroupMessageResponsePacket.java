package com.sanshengshui.im.protocol.response;

import com.sanshengshui.im.protocol.Packet;
import com.sanshengshui.im.session.Session;
import lombok.Data;

import static com.sanshengshui.im.protocol.command.Command.GROUP_MESSAGE_RESPONSE;


@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}
