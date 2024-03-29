package avatar.global.lockMsg;

/**
 * 锁信息
 */
public class LockMsg {
    /**
     * socket连接信息
     */
    //互通设备socket连接锁
    public static final String CONNECT_SOCKET_LINK_LOCK = "connectSocketLinkLock";
    //自研设备重连信息锁
    public static final String SYNC_INNO_RECONNECT_LOCK = "productInnoReconnectLock";
    //普通设备重连信息锁
    public static final String INNER_NORMAL_PRODUCT_RECONNECT_LOCK = "innerNormalProductReconnectLock";
    //设备session锁
    public static final String PRODUCT_SESSION_LOCK = "proSesLock";

    /**
     * 游戏信息
     */
    //玩家在线锁
    public static final String USER_ONLINE_LOCK = "userOnlineLock";
    //设备房间信息锁
    public static final String PRODUCT_ROOM_DEAL_LOCK = "productRoomDealLock";
    //自研设备中奖锁
    public static final String SELF_PRODUCT_AWARD_LOCK = "selfProductAwardLock";
    //设备消费锁
    public static final String PRODUCT_COST_COIN_LOCK = "productCostCoinLock";
    //设备掉币锁
    public static final String PRODUCT_PUSH_COIN_LOCK = "productPushCoinMsgLock";
    //自研特殊中奖锁
    public static final String INNO_SPECIAL_AWARD_LOCK = "innoSpecialAwardLock";

    /**
     * 玩家信息
     */
    //花费锁时间
    public static final String USER_COST_DEAL_LOCK = "userCostDealLock";
    //玩家属性锁
    public static final String USER_ATTRIBUTE_LOCK = "userAttributeLock";
    //道具锁
    public static final String PROPERTY_LOCK = "propertyLock";


}
