package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 充值接口路由
 */
public class RechargeHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    public static final String SHOPPING_MALL = Prefix+"/shp_mal";//商城中心
    public static final String COMMODITY_DIRECT_PURCHASE = Prefix+"/cmd_dr_puc";//商品直购
    public static final String REFRESH_MALL_PROPERTY = Prefix+"/rf_mal_ppy";//刷新商城道具

}
