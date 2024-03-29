package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * solana接口路由
 */
public class SolanaHttpCmdName {
    private static final String Prefix = ConfigMsg.SOLONA_ROUTE_PREFIX;//前缀

    public static final String CREATE_ARCX_ACCOUNT = Prefix+"/create_arcx_account";//创建代币账号
    public static final String ARCX_ACCOUNT_BALANCE = Prefix+"/arcx_account_balance";//arcx代币账号余额
    public static final String ARCX_TRANSFER = Prefix+"/arcx_transfer";//转账
    public static final String ARCX_TRANSFER_TRANSACTION = Prefix+"/arcx_transfer_transaction";//转账交易凭证

}
