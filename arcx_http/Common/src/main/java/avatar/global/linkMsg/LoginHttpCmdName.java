package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 登录请求路由
 */
public class LoginHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    public static final String LOGOUT_ACCOUNT = Prefix+"/lgo_act";//注销账号
    public static final String TOKEN_REFRESH = Prefix+"/ref_voucher";//凭证更新
    public static final String USER_LOGIN = Prefix+"/ply_log_on";//玩家登录
    public static final String EMAIL_VERIFY_CODE = Prefix+"/em_vrf_cd";//邮箱验证码


}
