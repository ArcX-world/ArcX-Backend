package avatar.util.sendMsg;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.net.session.Session;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserOnlineUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送websocket信息的工具类
 */
public class SendWebsocketMsgUtil {
    /**
     * 推送信息给玩家
     */
    public static void sendByUserId(int cmd, int status, int userId, JSONObject dataJson) {
        String accessToken = UserUtil.loadAccessToken(userId);//获取token
        if(!StrUtil.checkEmpty(accessToken)) {
            sendByAccessToken(cmd, status, accessToken, dataJson);
            if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {
                LogUtil.getLogger().info("推送命令{}给玩家{}，内容{}--------",
                        cmd, userId, JsonUtil.mapToJson(dataJson));
            }
        }
    }

    /**
     * 推送信息给玩家
     */
    public static void sendByAccessToken(int cmd, int status, String accessToken, JSONObject dataJson) {
        if(!StrUtil.checkEmpty(accessToken)) {
            int userId = UserUtil.loadUserIdByToken(accessToken);//玩家ID
            Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
            if (session != null) {
                Map<String, Object> map = new HashMap<>();
                //返回的参数
                map.put("errorCode", status);
                map.put("errorDesc", ClientCode.getErrorMsgByStatus(status));//错误信息
                map.put("serverTime", TimeUtil.getNowTimeStr());//当前时间
                map.put("serverDate", TimeUtil.getNowTime());//当前时间戳
                map.put("serverMsg", dataJson);//返回参数
                //直接推送客户端
                session.sendClientByAccessTokenRes(cmd, accessToken, map);
                if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {
                    LogUtil.getLogger().info("推送命令{}给玩家{}，内容{}--------",
                            cmd, userId, JsonUtil.mapToJson(map));
                }
            } else {
                LogUtil.getLogger().error("推送命令{}给玩家{}的时候玩家session空了", cmd, userId);
                //删除玩家在线信息
                UserOnlineUtil.delUserOnlineMsg(userId);
            }
        }
    }

    /**
     * 推送信息给玩家
     */
    public static void sendBySession(int cmd, int status, Session session, JSONObject dataJson) {
        if(session!=null){
            Map<String, Object> map = new HashMap<>();
            //返回的参数
            map.put("errorCode" , status);
            map.put("errorDesc", ClientCode.getErrorMsgByStatus(status));//错误信息
            map.put("serverTime", TimeUtil.getNowTimeStr());//当前时间
            map.put("serverDate", TimeUtil.getNowTime());//当前时间戳
            map.put("serverMsg", dataJson);//返回参数
            //直接推送客户端
            session.sendClientByAccessTokenRes(cmd, session.getAccessToken(), map);
            if (!StrUtil.strToList(ConfigMsg.noListenSocket).contains(cmd)) {
                LogUtil.getLogger().info("推送命令{}给玩家{}，内容{}--------",
                        cmd, UserUtil.loadUserIdByToken(session.getAccessToken()), JsonUtil.mapToJson(map));
            }
        }
    }

    /**
     * 关闭socket
     */
    public static void closeSocket(int userId) {
        String accessToken = UserUtil.loadAccessToken(userId);//获取token
        if(!StrUtil.checkEmpty(accessToken)) {
            while (true) {
                Session session = GameData.getSessionManager().getSessionByAccesstoken(accessToken);
                if (session != null) {
                    LogUtil.getLogger().info("服务端主动断掉玩家{}的websocket------", userId);
                    session.close();
                } else {
                    break;
                }
            }
        }
    }
}
