package avatar.global.basicConfig.basic;

/**
 * 系统配置信息
 */
public class ConfigMsg {
    //默认路由前缀
    public static final String DEFAULT_ROUTE_PREFIX = "/arcx_http";
    //默认socket服务端前缀
    public static final String DEFAULT_SERVER_ROUTE_PREFIX = "/arcx_socket";
    //solana服务端前缀
    public static final String SOLONA_ROUTE_PREFIX = "/api/arcx_solana";
    //当前平台
    public static final String sysPlatform = "arcxInternal";
    //普通商品
    public static final String normalCommodity = "1,2";
    //本地访问域名
    public static final String localHttpName = "https://test5.wonder.net.cn";
    //线上访问域名
    public static final String onlineHttpName = "https://tideplay.realcoinpusher.com";
    //验证码发送间隔
    public static final long verifyCodeSendTime = 60000;
    //验证码超时时间
    public static final long verifyCodeOutTime = 120000;
    //当天反馈上限
    public static final int userMaxOpinion=1;
    //app名称
    public static final String appName = "Arcx";

}
