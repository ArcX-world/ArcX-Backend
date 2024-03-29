//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package avatar.net.http;

import avatar.global.Config;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.net.BaseHandler;
import avatar.net.NetLog;
import avatar.net.SystemEvent;
import avatar.net.session.HttpTransport;
import avatar.net.session.Session;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.*;

public class HttpServerHandler extends BaseHandler {
    private static final String FAVICON_ICO = "/favicon.ico";

    public HttpServerHandler() {
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = GameData.getSessionManager().getSession(ctx.channel());
        if (session != null) {
            session.close();
        }

    }

    private Session initSession(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        HttpTransport session = new HttpTransport(ctx, fullHttpRequest);
        GameData.getSessionManager().registerConn(ctx.channel(), session);
        return session;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Session session = GameData.getSessionManager().getSession(ctx.channel());
        if (session != null) {
            session.close();
        }

    }

    public String getIp(Session session, FullHttpRequest fullHttpRequest) {
        HttpHeaders httpHeaders = fullHttpRequest.headers();
        String ip = httpHeaders.get("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.get("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.get("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.get("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpHeaders.get("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.equals("127.0.0.1") || "unknown".equalsIgnoreCase(ip)) {
            String identity = session.getIdentity().toString();
            if (identity != null && !"".equals(identity)) {
                ip = identity.substring(identity.lastIndexOf("/") + 1, identity.lastIndexOf("]"));
            }
        }

        if (ip != null && ip.contains(":")) {
            ip = ip.substring(0, ip.lastIndexOf(":"));
        }

        return ip;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
            String uri = fullHttpRequest.getUri();
            if (Config.getInstance().isWindowsOS() || !uri.equals("/favicon.ico")) {
                HttpPacket httpPacket = null;
                Session session = this.initSession(ctx, fullHttpRequest);
                String ip = this.getIp(session, fullHttpRequest);
                HttpMethod m = fullHttpRequest.getMethod();
                String accessToken = fullHttpRequest.headers().get("accessToken");
                Iterator var13;
                String contentType;
                Set keys;
                if (HttpMethod.GET.equals(m)) {
                    String resUri = this.getUri(uri);
                    if (PostMethod.isPost(resUri)) {
                        SendMsgUtil.sendBySessionAndMap(session, ClientCode.VERIFY_SIGN_ERROR.getCode(), new HashMap());
                    } else {
                        QueryStringDecoder decoderQuery = new QueryStringDecoder(uri);
                        contentType = decoderQuery.path();
                        Map<String, List<String>> uriAttributes = decoderQuery.parameters();
                        Map<String, Object> map = new HashMap();
                        map.put("accessToken", accessToken);
                        keys = uriAttributes.keySet();
                        var13 = keys.iterator();

                        while(var13.hasNext()) {
                            String key = (String)var13.next();
                            if (((List)uriAttributes.get(key)).size() > 0) {
                                map.put(key, ((List)uriAttributes.get(key)).get(0));
                            } else {
                                map.put(key, (Object)null);
                            }
                        }

                        int code = contentType.hashCode();
                        code = Math.abs(code);
                        map.put("ip", ip);
                        map.put("route", resUri);
                        httpPacket = new HttpPacket(contentType, code, map);
                        SystemEvent systemEvent = new SystemEvent(httpPacket, session);
                        GameData.getSessionManager().addNetEvent(systemEvent);
                    }
                } else if (HttpMethod.POST.equals(m)) {
                    Map<String, Object> uriAttributes = new HashMap();
                    HttpHeaders httpHeaders = fullHttpRequest.headers();
                    uriAttributes.put("accessToken", accessToken);
                    if (PostMethod.isNoCheckCallBack(uri)) {
                        contentType = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
                        uriAttributes.put("content", contentType);
                    } else {
                        contentType = httpHeaders.get("Content-Type");
                        if (contentType.indexOf("x-www-form-urlencoded") < 0 && contentType.indexOf("form-data") < 0) {
                            if (contentType.indexOf("application/json") >= 0) {
                                String jsonStr = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
                                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                                keys = jsonObject.keySet();
                                Iterator var29 = keys.iterator();

                                while(var29.hasNext()) {
                                    String key = (String)var29.next();
                                    uriAttributes.put(key, jsonObject.get(key));
                                }
                            }
                        } else {
                            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullHttpRequest);
                            decoder.offer(fullHttpRequest);
                            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
                            var13 = parmList.iterator();

                            while(var13.hasNext()) {
                                InterfaceHttpData parm = (InterfaceHttpData)var13.next();
                                Attribute data = (Attribute)parm;

                                try {
                                    uriAttributes.put(data.getName(), data.getValue());
                                } catch (IOException var24) {
                                    var24.printStackTrace();
                                }
                            }
                        }
                    }

                    boolean flag = true;
                    String platform = httpHeaders.get("platform");
                    if (!PostMethod.isCallBack(uri)) {
                        try {
                            String jsonStr = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
                            if (StrUtil.checkEmpty(jsonStr)) {
                                jsonStr = httpHeaders.get("jsonStr");
                            }

                            long timestamp = Long.parseLong(httpHeaders.get("timestamp"));
                            String code = httpHeaders.get("tidePlayBackPas");
                            String key = "tidePlayBack@20231114~!";
                            String str = timestamp + "+" + key + "+" + jsonStr + "+" + platform;
                            String encodeStr = MD5Util.MD5(str);
                            LogUtil.getLogger().info("接口{}的参数为：{},key为：{},platform为：{}，收到的加密为：{}，加密的字符串为：{}，加密后的密码为：{}---", new Object[]{uri, jsonStr, key, platform, code, str, encodeStr});
                            if (!code.equals(encodeStr)) {
                                flag = false;
                            }
                        } catch (Exception var23) {
                            flag = false;
                            var23.printStackTrace();
                        }
                    }

                    if (flag) {
                        if (uri.contains("?")) {
                            uri = uri.substring(0, uri.lastIndexOf("?"));
                        }

                        int code = uri.hashCode();
                        code = Math.abs(code);
                        if (!PostMethod.isNoConstainIp(uri)) {
                            if (!StrUtil.checkEmpty(ip) && ip.contains(",")) {
                                ip = ip.split(",")[1];
                                if (!StrUtil.checkEmpty(ip)) {
                                    ip = ip.trim();
                                }
                            }

                            uriAttributes.put("ip", ip);
                        }

                        uriAttributes.put("route", this.getUri(uri));
                        httpPacket = new HttpPacket(uri, code, uriAttributes);
                        SystemEvent systemEvent = new SystemEvent(httpPacket, session);
                        GameData.getSessionManager().addNetEvent(systemEvent);
                    } else {
                        LogUtil.getLogger().info("检测http的post接收参数的时候参数验证错误，报错接口{}，平台{}------", uri, platform);
                        SendMsgUtil.sendBySessionAndMap(session, ClientCode.VERIFY_SIGN_ERROR.getCode(), new HashMap());
                    }
                } else if (HttpMethod.OPTIONS.equals(m)) {
                    Channel identity = (Channel)session.getIdentity();
                    ByteBuf buf = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
                    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
                    response.headers().set("Content-Type", "application/json; charset=UTF-8");
                    response.headers().set("Content-Length", buf.readableBytes());
                    response.headers().set("Access-Control-Allow-Origin", "*");
                    response.headers().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                    response.headers().set("Access-Control-Max-Age", "3600");
                    response.headers().set("Access-Control-Allow-Headers", "Origin, X-Requested-With,x-requested-with, Content-Type, Accept, client_id, uuid, Authorization");
                    identity.writeAndFlush(response);
                    if (session != null && !StrUtil.checkEmpty(session.getAccessToken())) {
                        session.close();
                    }
                }
            }
        } else {
            NetLog.log.getLogger().error("[HttpServerHandler] messageReceived , ====== 0");
        }

    }

    public String getUri(String uri) {
        String retUri;
        if (uri.contains("?")) {
            retUri = uri.substring(0, uri.indexOf("?"));
        } else {
            retUri = uri;
        }

        return retUri;
    }
}
