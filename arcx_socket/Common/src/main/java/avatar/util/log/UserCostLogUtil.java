package avatar.util.log;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.global.code.basicConfig.UserCostMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductCostCoinMsgDao;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.ProductUserWeightNaTask;
import avatar.util.CommonUtil;
import avatar.util.LogUtil;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUtil;

/**
 * 玩家消费工具类
 */
public class UserCostLogUtil {
    /**
     * 游戏结束处理
     * 结算
     */
    public static void dealGamingResult(int userId, int productId) {
        String costMsg = ProductUtil.productLog(productId);//繁体日志信息
        long sumAddCoin = 0;//总掉币数
        long sumCostCoin = 0;//总扣除币
        //添加游戏币和彩票处理
        //获取设备锁
        RedisLock pushCoinLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_COST_COIN_LOCK+"_"+productId,
                2000);
        try {
            if (pushCoinLock.lock()) {
                ProductCostCoinMsg costCoinMsg = ProductCostCoinMsgDao.getInstance().loadByProductId(productId);
                if (costCoinMsg!=null) {
                    sumAddCoin = costCoinMsg.getSumAddCoin();//总掉币数
                    sumCostCoin = costCoinMsg.getSumCostCoin();//总扣币数
                    //更新缓存
                    ProductCostCoinMsgDao.getInstance().setCache(productId, ProductGamingUtil.initProductCostCoinMsg(productId));
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            pushCoinLock.unlock();
        }
        //消耗游戏币
        if(sumCostCoin>0){
            //处理日志信息
            dealLogMsg(userId, CommodityTypeEnum.GOLD_COIN.getCode(), sumCostCoin*-1, 0, costMsg);
        }
        if(sumAddCoin>0){
            //处理日志信息
            dealLogMsg(userId, CommodityTypeEnum.GOLD_COIN.getCode(), sumAddCoin, productId, costMsg);
        }
        //玩家权重NA值
        if(sumAddCoin-sumCostCoin>0){
            if(ProductUtil.isInnoProduct(productId)) {
                SchedulerSample.delayed(10,
                        new ProductUserWeightNaTask(productId, userId, sumAddCoin-sumCostCoin));
            }
        }
    }

    /**
     * 处理日志信息
     */
    public static void dealLogMsg(int userId, int commodityType, long awardNum, int productId, String costMsg){
        //添加操作日志
        UserOperateLogUtil.costBalance(awardNum, userId, productId, commodityType, costMsg);
    }

    /**
     * 添加龙珠奖励
     */
    public static void addDragonAward(int userId, int productId, int awardNum) {
        int commodityType = CommodityUtil.gold();//金币
        boolean flag = UserBalanceUtil.addUserBalance(userId, commodityType, awardNum);
        if (flag) {
            //添加日志
            UserCostLogUtil.dealLogMsg(userId, commodityType, awardNum, productId, UserCostMsg.dragonBall);
        } else {
            LogUtil.getLogger().error("添加玩家{}在设备{}推下龙珠奖励的{}金币失败--------", userId, productId, awardNum);
        }
    }

    /**
     * 龙珠玛丽机奖励
     */
    public static void dragonTrainAward(int userId, int productId, int awardNum, int awardType) {
        boolean flag = UserBalanceUtil.addUserBalance(userId, awardType, awardNum);
        if (flag) {
            //添加日志
            UserCostLogUtil.dealLogMsg(userId, awardType, awardNum, productId, UserCostMsg.dragonTrain);
        } else {
            LogUtil.getLogger().error("添加玩家{}在设备{}龙珠玛丽机奖励的{}{}失败--------", userId, productId, awardNum,
                    CommodityTypeEnum.getNameByCode(awardType));
        }
    }

    /**
     * 龙珠玛丽机道具奖励
     */
    public static void dragonTrainProperty(int userId, int awardId, int awardNum) {
        boolean flag = UserPropertyUtil.addUserProperty(userId, awardId, awardNum);
        if(flag){
            //添加操作日志
            UserOperateLogUtil.costProperty(awardNum, userId, awardId, UserCostMsg.dragonTrain);
        }else{
            LogUtil.getLogger().error("玩家{}充值的{}道具{}失败-------", userId, awardNum,
                    PropertyTypeEnum.getNameByCode(awardId));
        }
    }
}
