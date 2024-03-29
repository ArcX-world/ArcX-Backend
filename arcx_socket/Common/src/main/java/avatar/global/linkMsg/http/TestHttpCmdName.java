package avatar.global.linkMsg.http;

import avatar.global.code.basicConfig.ConfigMsg;

/**
 * 测试路由
 */
public class TestHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 龙珠玛丽机
     */
    public static final String INIT_INNO_DRAGON_TRAIN_USER_MSG = Prefix+"/init_inno_dragon_train_user_msg";//初始化自研龙珠玛丽机玩家信息
    public static final String ADD_INNO_DRAGON_BONUS = Prefix+"/add_inno_dragon_bonus";//添加自研龙珠奖励

    /**
     * 自研设备
     */
    public static final String INNO_PRODUCT_AWARD = Prefix+"/inno_product_award";//自研设备中奖

}
