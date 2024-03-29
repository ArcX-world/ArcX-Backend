package avatar.util.basic.general;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
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
     * 经验
     */
    public static int exp() {
        return CommodityTypeEnum.EXP.getCode();
    }

    /**
     * 获取奖励名称
     */
    public static String loadAwardName(int awardType, int awardId) {
        if(awardType>0) {
            String awardName;//奖励名称
            if(awardType==CommodityTypeEnum.PROPERTY.getCode()){
                awardName = PropertyTypeEnum.getNameByCode(awardId);
            }else{
                awardName = CommodityTypeEnum.getNameByCode(awardType);
            }
            return awardName;
        }else{
            return "";
        }
    }

}
