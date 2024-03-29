package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * NFT请求路由
 */
public class NftHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 充值
     */
    public static final String OPERATE_SELL_GOLD_MACHINE = Prefix+"/opr_sel_gd_mch";//销售中的售币机
    public static final String EXCHANGE_NFT_GOLD = Prefix+"/exc_nft_gd";//兑换NFT的金币
    /**
     * 背包
     */
    public static final String NFT_KNAPSACK = Prefix+"/nft_kpk";//NFT背包
    public static final String NFT_MSG = Prefix+"/nft_ifo";//NFT信息
    public static final String UPGRADE_NFT = Prefix+"/upg_nft";//NFT升级
    public static final String SELL_GOLD_MACHINE_ADD_GOLD = Prefix+"/sel_gd_mch_ad_gd";//售币机增加储币
    public static final String SELL_GOLD_MACHINE_REPAIR_DURABILITY = Prefix+"/sel_gd_mch_rp_drblt";//售币机维修耐久度
    public static final String SELL_GOLD_MACHINE_OPERATE = Prefix+"/sel_gd_mch_opr";//售币机营业
    public static final String SELL_GOLD_MACHINE_STOP_OPERATE = Prefix+"/sel_gd_mch_stp_opr";//售币机停止营业
    public static final String SELL_GOLD_MACHINE_LIST_MARKET = Prefix+"/sel_gd_mch_lst_mk";//售币机上架市场
    public static final String SELL_GOLD_MACHINE_CANCEL_MARKET = Prefix+"/sel_gd_mch_cacl_mk";//售币机取消上架市场
    public static final String NFT_REPORT = Prefix+"/nft_rp";//NFT报告

    /**
     * 市场
     */
    public static final String MARKET_NFT_LIST = Prefix+"/mk_nft_lst";//NFT市场列表
    public static final String MARKET_NFT_MSG = Prefix+"/mk_nft_ifo";//市场NFT信息
    public static final String BUY_NFT = Prefix+"/buy_nft";//购买NFT


}
