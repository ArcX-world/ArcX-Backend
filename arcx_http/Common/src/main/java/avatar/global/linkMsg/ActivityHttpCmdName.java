package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 活动请求路由
 */
public class ActivityHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 签到信息
     */
    public static final String SIGN_MSG = Prefix+"/sgn_ifo";//签到信息
    public static final String RECEIVE_SIGN_AWARD = Prefix+"/rc_sgn_awd";//领取签到奖励
}
