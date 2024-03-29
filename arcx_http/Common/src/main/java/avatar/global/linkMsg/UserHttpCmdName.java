package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 玩家请求路由
 */
public class UserHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 玩家信息
     */
    public static final String USER_INFO = Prefix+"/ply_ifo";//玩家信息
    public static final String UPDATE_USER_INFO = Prefix+"/ud_ply_ifo";//更新玩家信息
    public static final String UPDATE_USER_PASSWORD = Prefix+"/ud_ply_pwd";//更新玩家密码

    /**
     * 帮助与反馈
     */
    public static final String USER_OPINION = Prefix+"/ply_fb";//帮助与反馈

    /**
     * 联系信息
     */
    public static final String COMMUNICATE_MSG = Prefix+"/cmc_ifo";//联系信息

    /**
     * 背包信息
     */
    public static final String PROPERTY_KNAPSACK = Prefix+"/ppt_kpk";//道具背包

    /**
     * 玩家操作
     */
    public static final String UPGRADE_ATTRIBUTE = Prefix+"/upg_atb";//属性升级
    public static final String USE_PROPERTY = Prefix+"/use_ppt";//使用道具

    /**
     * 钱包
     */
    public static final String WALLET_SPENDING = Prefix+"/wl_spd";//钱包开销（中心化数据）
    public static final String CHAIN_WALLET = Prefix+"/chn_wl";//链上钱包（从链上获取）
    public static final String WALLET_WITHDRAW = Prefix+"/wl_wtd";//钱包提现（spending->wallet）
    public static final String WALLET_RECHARGE = Prefix+"/wl_rcg";//钱包充值（wallet->spending）
    public static final String TRANSFER_TOKENS = Prefix+"/tsf_tks";//代币转移（wallet->其他人）
    public static final String WALLET_CONFIG = Prefix+"/wl_cfg";//钱包配置

}
