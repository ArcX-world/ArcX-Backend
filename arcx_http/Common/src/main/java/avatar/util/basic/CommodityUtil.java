package avatar.util.basic;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
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

    /**
     * 金币
     */
    public static int gold(){
        return CommodityTypeEnum.GOLD_COIN.getCode();
    }

    /**
     * axc
     */
    public static int axc(){
        return CommodityTypeEnum.AXC.getCode();
    }

    /**
     * usdt
     */
    public static int usdt(){
        return CommodityTypeEnum.USDT.getCode();
    }

    /**
     * 道具
     */
    public static int property(){
        return CommodityTypeEnum.PROPERTY.getCode();
    }

    /**
     * 经验
     */
    public static int exp() {
        return CommodityTypeEnum.EXP.getCode();
    }
}
