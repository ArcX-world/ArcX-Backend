package avatar.util.user;

import avatar.global.basicConfig.basic.UserConfigMsg;
import avatar.util.system.TimeUtil;

import java.util.UUID;

/**
 * 玩家token工具类
 */
public class UserTokenUtil {
    /**
     * 初始化玩家的调用凭证
     */
    public static String initUserAccessToken(int userId) {
        return "aes"+(UUID.randomUUID().toString().replaceAll("-", ""))+userId;
    }

    /**
     * 获取新的玩家的调用凭证过期时间
     */
    public static long userAccessTokenOutTime(){
        return TimeUtil.getNHour(TimeUtil.getNowTimeStr(), UserConfigMsg.userAccessTokenOutTime*24);
    }

    /**
     * 初始化玩家的刷新凭证
     */
    public static String initUserRefreshToken(int userId) {
        return "ref"+(UUID.randomUUID().toString().replaceAll("-", ""))+userId;
    }

    /**
     * 获取新的玩家的刷新凭证过期时间
     */
    public static long userRefreshTokenOutTime(){
        return TimeUtil.getNHour(TimeUtil.getNowTimeStr(), UserConfigMsg.userRefreshTokenOutTime*24);
    }
}
