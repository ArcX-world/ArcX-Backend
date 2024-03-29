package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 通知请求路由
 */
public class NoticeHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_SERVER_ROUTE_PREFIX;//前缀

    /**
     * 刷新玩家余额
     */
    public static final String REFRESH_USER_BALANCE_NOTICE = Prefix+"/refresh_user_balance_notice";//刷新玩家余额通知
    public static final String REFRESH_USER_ENERGY_NOTICE = Prefix+"/refresh_user_energy_notice";//刷新玩家能量通知
}
