package avatar.global.linkMsg.websocket;

/**
 * websocket信息
 */
public class WebSocketCmd {
    /**
     * 通用模块
     */
    public static int C2S_HEART = 1001;//心跳
    public static int S2C_HEART = 1002;//心跳
    public static int C2S_USER_BALANCE = 1003;//玩家余额信息
    public static int S2C_USER_BALANCE = 1004;//玩家余额信息
    public static int S2C_SYSTEM_NOTICE = 1006;//系统通知

    /**
     * 玩家 1101
     */
    public static int C2S_ENERGY_MSG = 1101;//能量信息
    public static int S2C_ENERGY_MSG = 1102;//能量信息

    /**
     * 设备 1201
     */
    //刷新/通知信息
    public static int C2S_REFRESH_TIME = 1201;//刷新时间
    public static int S2C_REFRESH_TIME = 1202;//刷新时间
    public static int C2S_ROOM_MSG = 1203;//房间信息
    public static int S2C_ROOM_MSG = 1204;//房间信息
    public static int C2S_JOIN_PRODUCT = 1205;//进入设备
    public static int S2C_JOIN_PRODUCT = 1206;//进入设备
    public static int C2S_EXIT_PRODUCT = 1207;//退出设备
    public static int S2C_KICK_OUT_PRODUCT = 1208;//踢出设备
    public static int C2S_PRODUCT_LOTTERY_PROGRESS = 1217;//设备彩票进度
    public static int S2C_PRODUCT_LOTTERY_PROGRESS = 1218;//设备彩票进度
    public static int S2C_PILE_TOWER_AWARD = 1220;//设备炼金塔堆塔奖励
    public static int S2C_PILE_TOWER_STATUS_NOTICE = 1222;//设备炼金塔堆塔状态通知
    //中奖信息
    public static int S2C_INNO_WIN_PRIZE = 1210;//自研设备中奖
    public static int S2C_SETTLEMENT_WINDOW_NOTICE = 1212;//结算窗口通知
    public static int S2C_AWARD_SCORE_MULTI_NOTICE = 1214;//中奖得分倍数通知
    public static int S2C_PRODUCT_VOICE_NOTICE = 1216;//设备声音通知
    public static int C2S_INNO_DRAGON_MSG = 1225;//自研设备龙珠信息
    public static int S2C_INNO_DRAGON_MSG = 1226;//自研设备龙珠信息
    public static int S2C_INNO_DRAGON_AWARD_MSG = 1228;//自研设备龙珠中奖信息



    /**
     * 设备模块
     */
    public static int C2S_COIN_PUSHER_OPERATION = 2001;//发送推币机操作
    public static int S2C_COIN_PUSHER_OPERATION = 2002;//推送推币机操作结果
    public static int C2S_DOLL_MACHINE_OPERATION = 2003;//发送娃娃机操作
    public static int S2C_DOLL_MACHINE_OPERATION = 2004;//推送娃娃机操作结果
    public static int C2S_PRESENT_MACHINE_OPERATION = 2005;//发送礼品机操作
    public static int S2C_PRESENT_MACHINE_OPERATION = 2006;//推送礼品机操作结果
}
