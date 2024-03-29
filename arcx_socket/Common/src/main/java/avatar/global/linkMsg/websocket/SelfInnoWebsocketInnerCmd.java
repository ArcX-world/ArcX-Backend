package avatar.global.linkMsg.websocket;

/**
 * 自研设备websocket内部操作
 */
public class SelfInnoWebsocketInnerCmd {
    public static final int P2S_HEART = 60001;//设备到自研心跳
    public static final int S2P_HEART = 60002;//自研到设备心跳
    public static final int P2S_START_GAME = 60003;//设备到自研开始游戏
    public static final int S2P_START_GAME = 60004;//自研到设备开始游戏
    public static final int P2S_OPERATE_MSG = 60005;//设备到自研操作信息
    public static final int S2P_OPERATE_MSG = 60006;//自研到设备操作信息
    public static final int P2S_END_GAME = 60007;//设备到自研结束游戏
    public static final int P2S_START_GAME_GAMING_USER_CHECK = 60009;//设备到自研开始游戏玩家校验
    public static final int S2P_START_GAME_GAMING_USER_CHECK = 60010;//自研到设备开始游戏玩家校验
    public static final int P2S_PRODUCT_MSG = 60011;//设备到自研设备信息
    public static final int S2P_PRODUCT_MSG = 60012;//自研到设备设备信息
    public static final int S2P_GET_COIN = 60014;//自研到设备获得币（订阅）
    public static final int P2S_CHANGE_COIN_WEIGHT = 60015;//设备到自研变更权重等级
    public static final int S2P_AWARD_LOCK_DEAL = 60018;//自研到设备中奖锁处理
    public static final int P2S_AUTO_PUSH_COIN = 60019;//设备到自研自动投币
    public static final int S2P_SETTLEMENT_WINDOW = 60022;//自研到设备结算窗口通知
    public static final int S2P_AWARD_SCORE_MULTI = 60024;//自研到设备中奖得分倍数通知
    public static final int S2P_VOICE_NOTICE_MSG = 60028;//自研到设备声音通知信息（订阅）
    public static final int S2P_PRODUCT_AWARD_NOTICE_MSG = 60030;//自研到设备中奖通知信息（订阅）
    public static final int S2P_DRAGON_NOTICE_MSG = 60032;//自研到设备龙珠通知信息（订阅）

}
