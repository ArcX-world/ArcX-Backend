package avatar.global.basicConfig.basic;

/**
 * 玩家配置信息
 */
public class UserConfigMsg {
    //玩家调用凭证过期时间(天)
    public static final int userAccessTokenOutTime = 7;
    //玩家刷新凭证过期时间(天)
    public static final int userRefreshTokenOutTime = 15;
    //游客注册前缀
    public static final String touristRegisterPrefix = "T";
    //苹果注册前缀
    public static final String appleRegisterPrefix = "AP";
    //邮箱注册前缀
    public static final String emailRegisterPrefix = "EM";
    //昵称长度
    public static final int nickLength = 20;
    //密码长度
    public static final int passwordLength = 20;
}
