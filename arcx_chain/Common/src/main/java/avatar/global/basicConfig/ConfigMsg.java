package avatar.global.basicConfig;

/**
 * 系统配置信息
 */
public class ConfigMsg {
    //默认路由前缀
    public static final String DEFAULT_ROUTE_PREFIX = "/outsource_1_server";
    //默认socket服务端前缀
    public static final String DEFAULT_SERVER_ROUTE_PREFIX = "/arcx_socket";
    //当前平台
    public static final String sysPlatform = "arcxInternal";
    //不需要监听的socket
    public static String noListenSocket = "1002";
    //游客的accessToken
    public static final String touristAccessToken = "touristAes";
    //solana服务端前缀
    public static final String SOLONA_ROUTE_PREFIX = "/api/arcx_solana";
    //普通商品
    public static final String normalCommodity = "1,2";
    //本地访问域名
    public static final String localHttpName = "https://test5.wonder.net.cn";
    //线上访问域名
    public static final String onlineHttpName = "https://tideplay.realcoinpusher.com";
}
