package com.sanshengshui.im.protocol.response;

import com.sanshengshui.im.protocol.Packet;
import lombok.Data;

import static com.sanshengshui.im.protocol.command.Command.QUIT_GROUP_RESPONSE;


@Data
public class QuitGroupResponsePacket extends Packet {

    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_RESPONSE;
    }
}
