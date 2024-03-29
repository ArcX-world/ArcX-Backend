package avatar.global.linkMsg.websocket;

/**
 * websocket普通设备内部操作机器指令
 */
public class WebsocketInnerCmd {
    public static final int C2S_HEART = 10001;//服务端到设备端心跳
    public static final int S2C_HEART = 10002;//设备到服务端心跳
    public static final int C2S_START_GAME = 10003;//服务端到设备开始游戏
    public static final int S2C_START_GAME = 10004;//设备到服务端开始游戏
    public static final int C2S_SETTLEMENT = 10005;//服务端到设备只结算
    public static final int S2C_SETTLEMENT = 10006;//设备到服务端只结算
    public static final int C2S_OFF_LINE = 10007;//服务端到设备结算并且退出
    public static final int C2S_GET_COIN = 10009;//服务端到设备获得币
    public static final int S2C_GET_COIN = 10010;//设备到服务端获得币
    public static final int C2S_PRODUCT_OPERATE = 10011;//服务端到设备设备操作
    public static final int C2S_DOWN_CATCH = 10013;//服务端到设备下爪
    public static final int S2C_DOWN_CATCH = 10014;//设备到服务端下爪
    public static final int S2C_SETTLEMENT_REFRESH = 10020;//设备到服务端街机结算刷新
    public static final int C2S_AUTO_SHOOT = 10021;//服务端到设备自动发炮
    public static final int S2C_AUTO_SHOOT = 10022;//设备到服务端自动发炮
    public static final int C2S_CANCEL_AUTO_SHOOT = 10023;//服务端到设备取消自动发炮
    public static final int S2C_CANCEL_AUTO_SHOOT = 10024;//设备到服务端取消自动发炮
    public static final int S2C_COIN_PILE_TOWER = 10028;//设备到服务端堆塔中
    public static final int C2S_PRODUCT_MSG = 10031;//服务端到设备设备信息
    public static final int S2C_PRODUCT_MSG = 10032;//设备到服务端设备信息
    public static final int C2S_PRODUCT_OCCUPY_CHECK = 10033;//服务端到设备占用检测
    public static final int S2C_PRODUCT_OCCUPY_CHECK = 10034;//设备到服务端设备占用检测
}
