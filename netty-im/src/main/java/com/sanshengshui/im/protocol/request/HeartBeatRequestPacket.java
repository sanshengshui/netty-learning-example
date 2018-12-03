package com.sanshengshui.im.protocol.request;


import com.sanshengshui.im.protocol.Packet;

import static com.sanshengshui.im.protocol.command.Command.HEARTBEAT_REQUEST;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
