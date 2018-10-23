package com.sanshengshui.iot.protocol;

import cn.hutool.core.util.StrUtil;
import com.sanshengshui.iot.common.auth.GrozaAuthService;
import com.sanshengshui.iot.common.message.DupPubRelMessageStore;
import com.sanshengshui.iot.common.message.DupPublishMessageStore;
import com.sanshengshui.iot.common.message.GrozaDupPubRelMessageStoreService;
import com.sanshengshui.iot.common.message.GrozaDupPublishMessageStoreService;
import com.sanshengshui.iot.common.session.GrozaSessionStoreService;
import com.sanshengshui.iot.common.session.SessionStore;
import com.sanshengshui.iot.common.subscribe.GrozaSubscribeStoreService;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author james
 * @date 2018年10月21日 10:31
 */
@Slf4j
public class Connect {

    private GrozaAuthService grozaAuthService;

    private GrozaSessionStoreService grozaSessionStoreService;

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    private GrozaSubscribeStoreService grozaSubscribeStoreService;


    public Connect(GrozaAuthService grozaAuthService,
                   GrozaSessionStoreService grozaSessionStoreService,
                   GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService,
                   GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService,
                   GrozaSubscribeStoreService grozaSubscribeStoreService){
        this.grozaAuthService = grozaAuthService;
        this.grozaSessionStoreService = grozaSessionStoreService;
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
        this.grozaDupPubRelMessageStoreService = grozaDupPubRelMessageStoreService;
        this.grozaSubscribeStoreService = grozaSubscribeStoreService;
    }

    public void processConnect(Channel channel, MqttConnectMessage msg){
        // 消息解码器出现异常
        if (msg.decoderResult().isFailure()) {
            Throwable cause = msg.decoderResult().cause();
            if (cause instanceof MqttUnacceptableProtocolVersionException) {
                // 不支持的协议版本
                MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION, false), null);
                channel.writeAndFlush(connAckMessage);
                channel.close();
                return;
            } else if (cause instanceof MqttIdentifierRejectedException) {
                // 不合格的clientId
                MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                        new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false), null);
                channel.writeAndFlush(connAckMessage);
                channel.close();
                return;
            }
            channel.close();
            return;
        }
        // clientId为空或null的情况, 这里要求客户端必须提供clientId, 不管cleanSession是否为1, 此处没有参考标准协议实现
        if (StrUtil.isBlank(msg.payload().clientIdentifier())) {
            MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED, false), null);
            channel.writeAndFlush(connAckMessage);
            channel.close();
            return;
        }
        // 用户名和密码验证, 这里要求客户端连接时必须提供用户名和密码, 不管是否设置用户名标志和密码标志为1, 此处没有参考标准协议实现
        String username = msg.payload().userName();
        String password = msg.payload().passwordInBytes() == null ? null : new String(msg.payload().passwordInBytes(), CharsetUtil.UTF_8);
        if (!grozaAuthService.checkValid(username,password)) {
            MqttConnAckMessage connAckMessage = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false), null);
            channel.writeAndFlush(connAckMessage);
            channel.close();
            return;
        }
        // 如果会话中已存储这个新连接的clientId, 就关闭之前该clientId的连接
        if (grozaSessionStoreService.containsKey(msg.payload().clientIdentifier())){
            SessionStore sessionStore = grozaSessionStoreService.get(msg.payload().clientIdentifier());
            Channel previous = sessionStore.getChannel();
            Boolean cleanSession = sessionStore.isCleanSession();
            if (cleanSession){
                grozaSessionStoreService.remove(msg.payload().clientIdentifier());
                grozaSubscribeStoreService.removeForClient(msg.payload().clientIdentifier());
                grozaDupPublishMessageStoreService.removeByClient(msg.payload().clientIdentifier());
                grozaDupPubRelMessageStoreService.removeByClient(msg.payload().clientIdentifier());
            }
            previous.close();
        }
        //处理遗嘱信息
        SessionStore sessionStore = new SessionStore(msg.payload().clientIdentifier(), channel, msg.variableHeader().isCleanSession(), null);
        if (msg.variableHeader().isWillFlag()){
            MqttPublishMessage willMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH,false, MqttQoS.valueOf(msg.variableHeader().willQos()),msg.variableHeader().isWillRetain(),0),
                    new MqttPublishVariableHeader(msg.payload().willTopic(),0),
                    Unpooled.buffer().writeBytes(msg.payload().willMessageInBytes())
            );
            sessionStore.setWillMessage(willMessage);
        }
        //处理连接心跳包
        if (msg.variableHeader().keepAliveTimeSeconds() > 0){
            if (channel.pipeline().names().contains("idle")){
                channel.pipeline().remove("idle");
            }
            channel.pipeline().addFirst("idle",new IdleStateHandler(0, 0, Math.round(msg.variableHeader().keepAliveTimeSeconds() * 1.5f)));
        }
        //至此存储会话消息及返回接受客户端连接
        grozaSessionStoreService.put(msg.payload().clientIdentifier(),sessionStore);
        //将clientId存储到channel的map中
        channel.attr(AttributeKey.valueOf("clientId")).set(msg.payload().clientIdentifier());
        Boolean sessionPresent = grozaSessionStoreService.containsKey(msg.payload().clientIdentifier()) && !msg.variableHeader().isCleanSession();
        MqttConnAckMessage okResp = (MqttConnAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK,false,MqttQoS.AT_MOST_ONCE,false,0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED,sessionPresent),
                null
        );
        channel.writeAndFlush(okResp);
        log.info("CONNECT - clientId: {}, cleanSession: {}", msg.payload().clientIdentifier(), msg.variableHeader().isCleanSession());
        // 如果cleanSession为0, 需要重发同一clientId存储的未完成的QoS1和QoS2的DUP消息
        if (!msg.variableHeader().isCleanSession()){
            List<DupPublishMessageStore> dupPublishMessageStoreList = grozaDupPublishMessageStoreService.get(msg.payload().clientIdentifier());
            List<DupPubRelMessageStore> dupPubRelMessageStoreList = grozaDupPubRelMessageStoreService.get(msg.payload().clientIdentifier());
            dupPublishMessageStoreList.forEach(dupPublishMessageStore -> {
                MqttPublishMessage publishMessage = (MqttPublishMessage)MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBLISH,true,MqttQoS.valueOf(dupPublishMessageStore.getMqttQoS()),false,0),
                        new MqttPublishVariableHeader(dupPublishMessageStore.getTopic(),dupPublishMessageStore.getMessageId()),
                        Unpooled.buffer().writeBytes(dupPublishMessageStore.getMessageBytes())
                );
                channel.writeAndFlush(publishMessage);
            });
            dupPubRelMessageStoreList.forEach(dupPubRelMessageStore -> {
                MqttMessage pubRelMessage = MqttMessageFactory.newMessage(
                        new MqttFixedHeader(MqttMessageType.PUBREL,true,MqttQoS.AT_MOST_ONCE,false,0),
                        MqttMessageIdVariableHeader.from(dupPubRelMessageStore.getMessageId()),
                        null
                );
                channel.writeAndFlush(pubRelMessage);
            });
        }
    }
}
