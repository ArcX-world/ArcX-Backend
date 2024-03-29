package avatar.util.log;

import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.util.LogUtil;
import avatar.util.user.UserBalanceUtil;

/**
 * 消耗工具类
 */
public class CostUtil {
    /**
     * 添加福利签到奖励道具
     */
    public static void addWelfareSignCommodity(int userId, int commodityType, int awardNum) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if(flag){
            //添加日志
            UserCostLogUtil.welfareSign(userId, commodityType, awardNum);
        }else{
            LogUtil.getLogger().error("添加玩家{}福利签到奖励的{}{}失败------", userId, awardNum,
                    CommodityTypeEnum.getNameByCode(commodityType));
        }
    }
}
