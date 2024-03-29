package avatar.util.user;

import avatar.data.user.balance.UserOnlineScoreMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.entity.user.info.UserPlatformBalanceEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.info.UserOnlineScoreDao;
import avatar.module.user.info.UserPlatformBalanceDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家余额工具类
 */
public class UserBalanceUtil {
    /**
     * 填充玩家平台余额信息
     */
    public static UserPlatformBalanceEntity initUserPlatformBalanceEntity(int userId, int commodityType) {
        UserPlatformBalanceEntity entity = new UserPlatformBalanceEntity();
        entity.setUserId(userId);//玩家ID
        entity.setCommodityType(commodityType);//商品类型
        entity.setCommodityNum(0);//商品余额
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 获取余额
     * 平台数量+在线分数
     */
    public static long getUserBalance(int userId, int commodityType) {
        //查询平台信息
        UserPlatformBalanceEntity balanceEntity = UserPlatformBalanceDao.getInstance().loadByMsg(
                userId, commodityType);
        long totalNum = balanceEntity==null?0:balanceEntity.getCommodityNum();
        //查询在线分数信息
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        totalNum += (msg.getAddNum()-msg.getCostNum());
        return totalNum;
    }

    /**
     * 填充玩家在线分数信息
     */
    public static UserOnlineScoreMsg initUserOnlineScoreMsg(int userId, int commodityType) {
        UserOnlineScoreMsg msg = new UserOnlineScoreMsg();
        msg.setUserId(userId);//玩家ID
        msg.setCommodityType(commodityType);//商品类型
        msg.setAddNum(0);//增加数量
        msg.setCostNum(0);//扣除数量
        return msg;
    }

    /**
     * 添加玩家余额
     */
    public static boolean addUserBalance(int userId, int commodityType, long num) {
        if(num>0) {
            boolean flag = false;
            //获取玩家余额锁
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    if (dealDataFlag(userId)) {
                        //查询平台余额信息
                        UserPlatformBalanceEntity entity = UserPlatformBalanceDao.getInstance().loadByMsg(userId, commodityType);
                        if (entity != null) {
                            entity.setCommodityNum(entity.getCommodityNum() + num);//商品数量
                            flag = UserPlatformBalanceDao.getInstance().update(entity);
                        } else {
                            LogUtil.getLogger().error("添加玩家{}-{}-{}的时候，查询不到玩家平台余额信息-----", userId,
                                    CommodityTypeEnum.getNameByCode(commodityType), num);
                        }
                    } else {
                        //添加在线余额
                        addUserOnlineScore(userId, commodityType, num);
                        flag = true;
                    }
                    //刷新余额
                    UserNoticePushUtil.userBalancePush(userId);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
            return flag;
        }else{
            return true;
        }
    }

    /**
     * 是否直接操作余额
     * 系统维护/玩家不在线，则操作数据库
     */
    private static boolean dealDataFlag(int userId){
        return CheckUtil.isSystemMaintain() || !UserOnlineUtil.isOnline(userId);
    }

    /**
     * 增加玩家在线分数
     */
    private static void addUserOnlineScore(int userId, int commodityType, long commodityNum){
        //查询信息
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        msg.setAddNum(msg.getAddNum()+commodityNum);
        //更新缓存
        UserOnlineScoreDao.getInstance().setCache(userId, commodityType, msg);
    }

    /**
     * 扣除玩家余额
     */
    public static boolean costUserBalance(int userId, int commodityType, int costNum) {
        boolean flag = false;
        if(costNum==0){
            return true;
        }
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_COST_DEAL_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                if(getUserBalance(userId, commodityType)>=costNum) {
                    if (dealDataFlag(userId)) {
                        //查询平台余额信息
                        UserPlatformBalanceEntity entity = UserPlatformBalanceDao.getInstance().loadByMsg(userId, commodityType);
                        if (entity != null) {
                            long commodityNum = entity.getCommodityNum();//玩家商品数量
                            entity.setCommodityNum(Math.max(0, (commodityNum - costNum)));//商品数量
                            flag = UserPlatformBalanceDao.getInstance().update(entity);
                        } else {
                            LogUtil.getLogger().error("扣除玩家{}-{}-{}的时候，查询不到玩家平台余额信息-----", userId,
                                    CommodityTypeEnum.getNameByCode(commodityType), costNum);
                        }
                    } else {
                        //扣除在线余额
                        costUserOnlineScore(userId, commodityType, costNum);
                        flag = true;
                    }
                    //刷新余额
                    UserNoticePushUtil.userBalancePush(userId);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }

    /**
     * 扣除玩家在线分数
     */
    private static void costUserOnlineScore(int userId, int commodityType, long commodityNum){
        //查询信息
        UserOnlineScoreMsg msg = UserOnlineScoreDao.getInstance().loadByMsg(userId, commodityType);
        msg.setCostNum(msg.getCostNum()+commodityNum);
        //更新缓存
        UserOnlineScoreDao.getInstance().setCache(userId, commodityType, msg);
    }

    /**
     * 扣除上机消费
     */
    public static boolean costStartGame(int productId, int userId, int cost) {
        boolean flag;
        if(cost==0){
            flag = true;
        }else {
            //查询设备信息
            ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
            int commodityType = entity.getCostCommodityType();//扣除商品类型
            if(commodityType==0){
                commodityType = CommodityTypeEnum.GOLD_COIN.getCode();//默认金币
            }
            flag = costUserBalance(userId, commodityType, cost);
            if(flag){
                //添加日志
                UserCostLogUtil.dealLogMsg(userId, commodityType, cost*-1, productId, ProductUtil.productLog(productId));
            }
        }
        return flag;
    }

}
