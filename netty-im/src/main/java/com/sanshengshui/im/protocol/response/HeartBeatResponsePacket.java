package com.sanshengshui.im.protocol.response;


import com.sanshengshui.im.protocol.Packet;

import static com.sanshengshui.im.protocol.command.Command.HEARTBEAT_RESPONSE;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
