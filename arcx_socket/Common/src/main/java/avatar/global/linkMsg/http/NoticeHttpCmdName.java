package avatar.global.linkMsg.http;

import avatar.global.code.basicConfig.ConfigMsg;

/**
 * 通知请求路由
 */
public class NoticeHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 系统
     */
    public static final String SYSTEM_NOTICE = Prefix+"/system_notice";//系统通知

    /**
     * 设备
     */
    public static final String KICK_OUT_PRODUCT_NOTICE = Prefix+"/kick_out_product_notice";//踢出设备通知
    public static final String REFRESH_ROOM_PUSH = Prefix+"/refresh_room_push";//刷新房间推送

    /**
     * 刷新玩家信息
     */
    public static final String REFRESH_USER_BALANCE_NOTICE = Prefix+"/refresh_user_balance_notice";//刷新玩家余额通知
    public static final String REFRESH_USER_ENERGY_NOTICE = Prefix+"/refresh_user_energy_notice";//刷新玩家能量通知
}
