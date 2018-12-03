package com.sanshengshui.im.protocol.response;

import com.sanshengshui.im.protocol.Packet;
import com.sanshengshui.im.session.Session;
import lombok.Data;

import java.util.List;

import static com.sanshengshui.im.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;


@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
