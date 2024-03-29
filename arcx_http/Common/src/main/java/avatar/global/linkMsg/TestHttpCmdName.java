package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 测试接口请求路由
 */
public class TestHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 测试
     */
    public static final String SERVER_TEST = Prefix+"/server_test";//服务测试

    /**
     * 签到
     */
    public static final String CLEAR_SIGN_MSG_TEST = Prefix+"/clear_sign_msg_test";//清除签到信息
}
