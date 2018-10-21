package com.sanshengshui.iot.protocol;

import com.sanshengshui.iot.common.auth.GrozaAuthService;
import com.sanshengshui.iot.common.message.GrozaDupPubRelMessageStoreService;
import com.sanshengshui.iot.common.message.GrozaDupPublishMessageStoreService;
import com.sanshengshui.iot.common.message.GrozaMessageIdService;
import com.sanshengshui.iot.common.message.GrozaRetainMessageStoreService;
import com.sanshengshui.iot.common.session.GrozaSessionStoreService;
import com.sanshengshui.iot.common.subscribe.GrozaSubscribeStoreService;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 协议处理
 */
@Component
public class ProtocolProcess {
    @Autowired
    private GrozaSessionStoreService grozaSessionStoreService;
    @Autowired
    private GrozaSubscribeStoreService grozaSubscribeStoreService;
    @Autowired
    private GrozaAuthService grozaAuthService;
    @Autowired
    private GrozaMessageIdService grozaMessageIdService;
    @Autowired
    private GrozaRetainMessageStoreService grozaRetainMessageStoreService;
    @Autowired
    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;
    @Autowired
    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    private ChannelGroup channelGroup;

    private Map<String, ChannelId> channelIdMap;

    private Connect connect;

    private Subscribe subscribe;

    private UnSubscribe unSubscribe;

    private Publish publish;

    private DisConnect disConnect;

    private PingReq pingReq;

    private PubRel pubRel;

    private PubAck pubAck;

    private PubRec pubRec;

    private PubComp pubComp;

    public Connect connect(){
        if (connect == null){
            connect = new Connect(grozaAuthService, grozaSessionStoreService, grozaDupPublishMessageStoreService, grozaDupPubRelMessageStoreService, grozaSubscribeStoreService, channelGroup, channelIdMap);
        }
        return connect;
    }
    public Subscribe subscribe(){
        if (subscribe == null){
            subscribe = new Subscribe(grozaSubscribeStoreService,grozaMessageIdService,grozaRetainMessageStoreService);
        }
        return subscribe;
    }
    public UnSubscribe unSubscribe() {
        if (unSubscribe == null) {
            unSubscribe = new UnSubscribe(grozaSubscribeStoreService);
        }
        return unSubscribe;
    }

    public Publish publish() {
        if (publish == null) {
            publish = new Publish(grozaSessionStoreService, grozaSubscribeStoreService, grozaMessageIdService, grozaRetainMessageStoreService, grozaDupPublishMessageStoreService, channelGroup, channelIdMap);
        }
        return publish;
    }

    public DisConnect disConnect() {
        if (disConnect == null) {
            disConnect = new DisConnect(grozaSessionStoreService, grozaSubscribeStoreService, grozaDupPublishMessageStoreService, grozaDupPubRelMessageStoreService);
        }
        return disConnect;
    }

    public PingReq pingReq() {
        if (pingReq == null) {
            pingReq = new PingReq();
        }
        return pingReq;
    }

    public PubRel pubRel() {
        if (pubRel == null) {
            pubRel = new PubRel();
        }
        return pubRel;
    }

    public PubAck pubAck() {
        if (pubAck == null) {
            pubAck = new PubAck(grozaDupPublishMessageStoreService);
        }
        return pubAck;
    }

    public PubRec pubRec() {
        if (pubRec == null) {
            pubRec = new PubRec(grozaDupPublishMessageStoreService, grozaDupPubRelMessageStoreService);
        }
        return pubRec;
    }

    public PubComp pubComp() {
        if (pubComp == null) {
            pubComp = new PubComp(grozaDupPubRelMessageStoreService);
        }
        return pubComp;
    }

    public GrozaSessionStoreService getGrozaSessionStoreService() {
        return grozaSessionStoreService;
    }
}
