package avatar.global.linkMsg.http;


import avatar.global.basicConfig.ConfigMsg;

/**
 * solana接口路由
 */
public class SolanaHttpCmdName {
    private static final String Prefix = ConfigMsg.SOLONA_ROUTE_PREFIX;//前缀

    public static final String LOAD_TRANSACTION = Prefix+"/load_transtraction";//获取交易信息
}
