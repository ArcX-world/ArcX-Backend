package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 跨服路由接口
 */
public class CrossServerHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    public static final String CROSS_SERVER_USER_MSG = Prefix+"/cross_server_user_msg";//跨服玩家信息
}
