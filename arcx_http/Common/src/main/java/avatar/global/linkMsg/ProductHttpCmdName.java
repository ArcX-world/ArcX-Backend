package avatar.global.linkMsg;

import avatar.global.basicConfig.basic.ConfigMsg;

/**
 * 设备接口路由
 */
public class ProductHttpCmdName {
    private static final String Prefix = ConfigMsg.DEFAULT_ROUTE_PREFIX;//前缀

    /**
     * 设备信息
     */
    public static final String PRODUCT_MSG = Prefix+"/dev_ifo";//设备信息
    public static final String PRODUCT_LIST = Prefix+"/dev_tbln";//设备列表
    public static final String FAST_JOIN_PRODUCT = Prefix+"/fs_jn_dev";//快速加入的设备
    public static final String GAMING_PRODUCT = Prefix+"/gm_dev";//游戏中的设备

    /**
     * 报修
     */
    public static final String REPAIR_PRODUCT = Prefix+"/dev_rp";//设备报修
}
