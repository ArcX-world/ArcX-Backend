package avatar.util.sendMsg;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.net.session.Session;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送信息的工具类
 */
public class SendMsgUtil {
    /**
     * 发送信息
     * @param session 会话ID
     * @param status 状态
     * @param data 核心数据
     */
    public static void sendBySessionAndMap(Session session, int status, Map<String,Object> data){
        Map<String,Object> map = new HashMap<>();
        map.put("status", status);//错误码
        map.put("errorMsg", ClientCode.getErrorMsgByStatus(status));//错误信息
        map.put("time",(new Date()).getTime());//当前时间戳
        map.put("data", data);//核心数据
        //将Map转换成json
        String jsonStr = JsonUtil.mapToJson(map);
        //发送回客户端
        sendHttpJson(session, jsonStr);
    }

    /**
     * 推送客户端
     */
    public static void sendHttpJson(Session session, String jsonStr){
        Channel identity = (Channel) session.getIdentity();
        ByteBuf buf = Unpooled.copiedBuffer(jsonStr, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set("Content-Type", "application/json; charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());
        response.headers().set("Access-Control-Allow-Origin", "*");
        response.headers().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.headers().set("Access-Control-Max-Age", "3600");
        response.headers().set("Access-Control-Allow-Headers", "Origin, X-Requested-With,x-requested-with, Content-Type, Accept, client_id, uuid, Authorization");
        identity.writeAndFlush(response);
        if(session!=null && !StrUtil.checkEmpty(session.getAccessToken())){
            session.close();
        }
    }
}
