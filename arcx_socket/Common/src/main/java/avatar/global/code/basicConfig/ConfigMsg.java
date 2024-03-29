package avatar.global.code.basicConfig;

/**
 * 系统配置信息
 */
public class ConfigMsg {
    //默认路由前缀
    public static final String DEFAULT_ROUTE_PREFIX = "/arcx_socket";
    //当前平台
    public static final String sysPlatform = "arcxInternal";
    //不需要监听的socket
    public static String noListenSocket = "1002,1004,1202,1204,1210,1212,1214,1216";
    //游客的accessToken
    public static final String touristAccessToken = "touristAes";
    //app名称
    public static final String appName = "Arcx";
    //进群设备不提示时间（秒）
    public static int joinProductTime=300;
    //普通商品
    public static final String normalCommodity = "1,2";
}
