package avatar.util.basic;

import avatar.global.basicConfig.ConfigMsg;
import avatar.util.system.StrUtil;

/**
 * 道具工具类
 */
public class CommodityUtil {
    /**
     * 是否普通商品
     */
    public static boolean normalFlag(int commodityType){
        return StrUtil.strToList(ConfigMsg.normalCommodity).contains(commodityType);
    }
}
