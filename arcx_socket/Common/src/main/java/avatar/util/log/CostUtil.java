package avatar.util.log;

import avatar.global.code.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.user.UserBalanceUtil;

/**
 * 消耗工具类
 */
public class CostUtil {
    /**
     * 炼金塔堆塔奖励
     */
    public static void addProductPileTowerAward(int userId, int productId, int awardNum) {
        int commodityType = CommodityTypeEnum.GOLD_COIN.getCode();//金币
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if (flag) {
            //添加堆塔玩家奖励信息
            ProductPileTowerUserMsgDao.getInstance().insert(
                    ProductPileTowerUtil.initProductPileTowerUserMsgEntity(userId, productId, awardNum));
            //添加日志
            UserCostLogUtil.dealLogMsg(userId, commodityType, awardNum, productId, UserCostMsg.pileTower);
        } else {
            LogUtil.getLogger().error("添加玩家{}在设备{}炼金塔堆塔奖励的{}金币失败--------", userId, productId, awardNum);
        }
    }

    /**
     * 添加设备奖励
     */
    public static void addProductCommodityCoin(int userId, int productId, int commodityType, int presentCoin) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, presentCoin);
        if (flag) {
            //添加日志
            UserCostLogUtil.dealLogMsg(userId, commodityType, presentCoin, productId, UserCostMsg.productAward);
        } else {
            LogUtil.getLogger().error("添加玩家{}在设备{}获得的{}-{}失败--------", userId, productId,
                    CommodityTypeEnum.getNameByCode(commodityType), presentCoin);
        }
    }

    /**
     * 添加能量兑换奖励
     */
    public static void addEnergyExchange(int userId, int productId, int commodityType, int presentCoin) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, presentCoin);
        if (flag) {
            //添加日志
            UserCostLogUtil.dealLogMsg(userId, commodityType, presentCoin, productId, UserCostMsg.energyExchange);
        } else {
            LogUtil.getLogger().error("添加玩家{}在设备{}进行能量兑换获得的{}-{}失败--------", userId, productId,
                    CommodityTypeEnum.getNameByCode(commodityType), presentCoin);
        }
    }
}
