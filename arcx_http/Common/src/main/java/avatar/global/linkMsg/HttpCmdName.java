package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

public class HttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 第三方
     */
    public static final String APPLE_USER_INFO_LOAD = "/login/apple_user_info_load";//苹果用户信息获取

    /**
     * 代理
     */
    public static final String AGENT_MSG = Prefix+"/ag_ifo";//代理信息
}

