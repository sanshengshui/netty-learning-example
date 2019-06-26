package com.sanshengshui.persistence.netty;

import com.alibaba.fastjson.JSONObject;
import com.sanshengshui.persistence.device.DeviceService;
import com.sanshengshui.persistence.domain.Device;
import io.netty.channel.*;

import java.net.InetAddress;
import java.util.Date;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private final DeviceService deviceService;

    public NettyServerHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        try {
            JSONObject jsonObject = JSONObject.parseObject(request);
            Device device = jsonObject.toJavaObject(Device.class);
            deviceService.saveDevice(device);
            ctx.write("Successfully saved!\r\n");
        } catch (Exception e) {
            ctx.write("error Json format!\r\n");
            e.printStackTrace();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
