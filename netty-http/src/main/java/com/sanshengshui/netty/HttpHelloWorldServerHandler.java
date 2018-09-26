package com.sanshengshui.netty;


import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author james
 */
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger logger = LoggerFactory.getLogger(HttpHelloWorldServerHandler.class);

    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };


    private HttpHeaders headers;
    private HttpRequest request;
    private FullHttpRequest fullRequest;
    private FullHttpResponse response;
    private HttpPostRequestDecoder decoder;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest){
            request = (HttpRequest) msg;
            headers = request.headers();
            String uri = request.uri();
            logger.info("http uri: "+ uri);
            if (uri.equals(FAVICON_ICO)){
                return;
            }
            HttpMethod method = request.method();
            if (method.equals(HttpMethod.GET)){
                QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
                Map<String, List<String>> uriAttributes = queryDecoder.parameters();
                //此处仅打印请求参数（你可以根据业务需求自定义处理）
                for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                    for (String attrVal : attr.getValue()) {
                        logger.info(attr.getKey() + "=" + attrVal);
                    }
                }
            }else if (method.equals(HttpMethod.POST)){
                //POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
                fullRequest = (FullHttpRequest)msg;
                //根据不同的Content_Type处理body数据
                dealWithContentType();

            }
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 简单处理常用几种 Content-Type 的 POST 内容（可自行扩展）
     * @throws Exception
     */
    private void dealWithContentType() throws Exception{
        String contentType = getContentType();
        //可以使用HttpJsonDecoder
        if(contentType.equals("application/json")){
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            JSONObject obj = JSON.parseObject(jsonStr);
            for(Map.Entry<String, Object> item : obj.entrySet()){
                System.out.println(item.getKey()+"="+item.getValue().toString());
            }

        }else if(contentType.equals("application/x-www-form-urlencoded")){
            //方式一：使用 QueryStringDecoder
			String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
			QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
			Map<String, List<String>> uriAttributes = queryDecoder.parameters();
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    System.out.println(attr.getKey() + "=" + attrVal);
                }
            }

        }else if(contentType.equals("multipart/form-data")){
            //TODO 用于文件上传
        }else{
            //do nothing...
        }
    }
    private String getContentType(){
        String typeStr = headers.get("Content-Type").toString();
        String[] list = typeStr.split(";");
        return list[0];
    }

}
