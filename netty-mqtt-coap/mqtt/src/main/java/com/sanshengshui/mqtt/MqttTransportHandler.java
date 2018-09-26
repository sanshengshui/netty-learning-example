package com.sanshengshui.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MqttTransportHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<Future<? super Void>> {
    public static final MqttQoS MAX_SUPPORTED_QOS_LVL = MqttQoS.AT_LEAST_ONCE;
    private volatile InetSocketAddress address;
    private AttributeKey<Boolean> _login = AttributeKey.valueOf("login");
    private AttributeKey<String> _deviceId = AttributeKey.valueOf("deviceId");

    private final int keepaliveSeconds;

    private ScheduledFuture<?> pingRespTimeout;

    public MqttTransportHandler(int keepaliveSeconds) {
        this.keepaliveSeconds = keepaliveSeconds;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.trace("[{}] Processing msg: {}", msg);
        if (msg instanceof MqttMessage) {
            processMqttMsg(ctx, (MqttMessage) msg);
        } else {
            ctx.close();
        }
    }

    private void processMqttMsg(ChannelHandlerContext ctx, MqttMessage msg) {
        address = (InetSocketAddress) ctx.channel().remoteAddress();
        if (msg.fixedHeader() == null) {
            log.info("[{}:{}] Invalid message received", address.getHostName(), address.getPort());
            processDisconnect(ctx);
            return;
        }

        /*if (quotaService.isQuotaExceeded(address.getHostName())) {
            log.warn("MQTT Quota exceeded for [{}:{}] . Disconnect", address.getHostName(), address.getPort());
            processDisconnect(ctx);
            return;
        }*/

        if(msg.fixedHeader().messageType().equals(MqttMessageType.CONNECT)) {
            processConnect(ctx, (MqttConnectMessage) msg);
        }

        Channel channel = ctx.channel();
        if (channel.hasAttr(_login)) {
            switch (msg.fixedHeader().messageType()) {
                case PUBLISH:
                    processPublish(ctx, (MqttPublishMessage) msg);
                    break;
                case SUBSCRIBE:
                    processSubscribe(ctx, (MqttSubscribeMessage) msg);
                    break;
                case UNSUBSCRIBE:
                    processUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                    break;
                case PINGREQ:
                    handlePingReq(ctx.channel());
                    break;
                case PINGRESP:
                    handlePingResp();
                    break;
                case DISCONNECT:
                    log.info("disconnect");
                    if (checkConnected(ctx)) {
                        processDisconnect(ctx);
                    }
                    break;
                default:
                    break;
            }
        }
    }



    private void processConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {

        // 此处可以对设备进行校验，根据token或者其他
        String userName = msg.payload().userName();
        // String password = msg.payload().password();
        String devId = msg.payload().clientIdentifier();
        // 此外默认是校验成功，等待确定好校验方式
        if (StringUtils.isEmpty(userName)) {
            ctx.writeAndFlush(createMqttConnAckMsg(
                    MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD));
            ctx.close();
        } else {
            // 此处登录成功
            Channel channel = ctx.channel();
            channel.attr(_login).set(true);
            channel.attr(_deviceId).set(devId);
            ctx.writeAndFlush(createMqttConnAckMsg(MqttConnectReturnCode.CONNECTION_ACCEPTED));
        }

    }


    private void processPublish(ChannelHandlerContext ctx, MqttPublishMessage mqttMsg) {
        //确保这个通道是可用的
        if (!checkConnected(ctx)) {
            return;
        }
        Channel channel = ctx.channel();
        String topicName = mqttMsg.variableHeader().topicName();
        String deviceId = channel.attr(_deviceId).get();
        //以后网关和设备的
        ByteBuf payLoad = mqttMsg.payload();
        String payload = payLoad.toString(Charset.forName("UTF-8"));
        if(StringUtils.isEmpty(topicName)) {
            return;
        }else if("/device/status".equals(topicName)) {
        }else if("/device/paramValue".equals(topicName)) {
        }else {
            //do nothing
        }

    }

    private void processSubscribe(ChannelHandlerContext ctx, MqttSubscribeMessage mqttMsg) {
        if (!checkConnected(ctx)) {
            return;
        }
        log.trace("Processing subscription [{}]!", mqttMsg.variableHeader().messageId());
        List<Integer> grantedQoSList = new ArrayList<>();

        for (MqttTopicSubscription subscription : mqttMsg.payload().topicSubscriptions()) {
            String topicName = subscription.topicName();
            //TODO: handle this qos level.
            MqttQoS reqQoS = subscription.qualityOfService();

            try {
                if (topicName.equals("")) {

                }else if (topicName.equals("")) {

                }
            }catch (Exception e) {
                log.warn("Failed to subscribe to [{}][{}]", topicName, reqQoS);
                grantedQoSList.add(MqttQoS.FAILURE.value());
            }
        }
        ctx.writeAndFlush(createSubAckMessage(mqttMsg.variableHeader().messageId(), grantedQoSList));
    }

    private void processUnsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage mqttMsg) {
        if (!checkConnected(ctx)) {
            return;
        }
        log.trace("Processing subscription [{}]!", mqttMsg.variableHeader().messageId());
        for (String topicName : mqttMsg.payload().topics()) {
            try {
                if (topicName.equals("")) {

                }
            } catch (Exception e) {
                log.warn("Failed to process unsubscription [{}] to [{}]",
                        mqttMsg.variableHeader().messageId(), topicName);
            }
        }

        ctx.writeAndFlush(createUnSubAckMessage(mqttMsg.variableHeader().messageId()));
    }

    private void processDisconnect(ChannelHandlerContext ctx) {
        log.info("processDisconnect");
        ctx.close();
    }


    private MqttConnAckMessage createMqttConnAckMsg(MqttConnectReturnCode returnCode) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false,
                MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(
                returnCode, true);
        return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
    }

    private static MqttSubAckMessage createSubAckMessage(Integer msgId, List<Integer> grantedQoSList) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        MqttSubAckPayload mqttSubAckPayload = new MqttSubAckPayload(grantedQoSList);
        return new MqttSubAckMessage(mqttFixedHeader, mqttMessageIdVariableHeader, mqttSubAckPayload);
    }

    private MqttMessage createUnSubAckMessage(int msgId) {
        MqttFixedHeader mqttFixedHeader =
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttMessageIdVariableHeader mqttMessageIdVariableHeader = MqttMessageIdVariableHeader.from(msgId);
        return new MqttMessage(mqttFixedHeader, mqttMessageIdVariableHeader);
    }

    private boolean checkConnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        if (channel.hasAttr(_login)) {
            return true;
        } else {
            log.info("[{}] Closing current session due to invalid msg order [{}][{}]");
            ctx.close();
            return false;
        }
    }

    private void sendPingReq(Channel channel){
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
        channel.writeAndFlush(new MqttMessage(fixedHeader));

        if(this.pingRespTimeout == null){
            this.pingRespTimeout = channel.eventLoop().schedule(() -> {
                MqttFixedHeader fixedHeader2 = new MqttFixedHeader(MqttMessageType.DISCONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
                channel.writeAndFlush(new MqttMessage(fixedHeader2)).addListener(ChannelFutureListener.CLOSE);
            }, this.keepaliveSeconds, TimeUnit.SECONDS);
        }
    }

    private void handlePingReq(Channel channel){
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        channel.writeAndFlush(new MqttMessage(fixedHeader));
    }

    private void handlePingResp(){
        log.info("handlePingResp");
        if(this.pingRespTimeout != null && !this.pingRespTimeout.isCancelled() && !this.pingRespTimeout.isDone()){
            this.pingRespTimeout.cancel(true);
            this.pingRespTimeout = null;
        }else{
            log.info("handlePingResp error");
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("【DefaultMqttHandler：channelActive】"+ctx.channel().remoteAddress().toString()+"链接成功");
        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

//        if(!ctx.isRemoved()){
//            String deviceId = ctx.channel().attr(_deviceId).get();
//            log.error("device [{}] Unexpected Exception", deviceId);
//            MqttHandlerService.dealDisConnected(deviceId);
//            ctx.close();
//        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            switch(event.state()){
//                case READER_IDLE:
//                    this.sendPingReq(ctx.channel());
//                    break;
                case WRITER_IDLE:
                    this.sendPingReq(ctx.channel());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {
        log.info("operationComplete");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);

        if(!ctx.isRemoved()){
            String deviceId = ctx.channel().attr(_deviceId).get();
            ctx.close();
        }
        log.info("channelUnregistered");
    }
}
