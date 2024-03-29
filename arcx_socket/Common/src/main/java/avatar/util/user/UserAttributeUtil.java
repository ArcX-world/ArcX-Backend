package avatar.util.user;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.user.attribute.UserEnergyMsg;
import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.product.energy.EnergyExchangeAwardEntity;
import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.entity.product.energy.EnergyExchangeUserHistoryEntity;
import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.energy.EnergyExchangeAwardDao;
import avatar.module.product.energy.EnergyExchangeProductDao;
import avatar.module.product.energy.EnergyExchangeUserAwardDao;
import avatar.module.product.energy.EnergyExchangeUserHistoryDao;
import avatar.module.user.attribute.UserAttributeConfigDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.attribute.UserGameExpConfigDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.AddEnergyAwardTask;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家属性工具类
 */
public class UserAttributeUtil {
    /**
     * 填充玩家属性配置实体信息
     */
    public static UserAttributeMsgEntity initUserAttributeMsgEntity(int userId) {
        UserAttributeMsgEntity entity = new UserAttributeMsgEntity();
        entity.setUserId(userId);//玩家ID
        int defaultLevel = 1;//默认等级
        entity.setUserLevel(defaultLevel);//玩家等级
        entity.setUserLevelExp(0);//玩家等级经验
        entity.setEnergyLevel(defaultLevel);//能量等级
        entity.setEnergyNum(0);//能量数量
        entity.setChargingLevel(defaultLevel);//充能等级
        entity.setChargingTime(TimeUtil.getNowTimeStr());//充能时间
        entity.setAirdropLevel(defaultLevel);//空投等级
        entity.setLuckyLevel(defaultLevel);//幸运等级
        entity.setCharmLevel(defaultLevel);//魅力等级
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 查询玩家经验数
     */
    public static long loadExpNum(int userId) {
        //查询玩家属性信息
        UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
        return entity==null?0:entity.getUserLevelExp();
    }

    /**
     * 添加玩家游戏币经验
     */
    public static void addUserGameExp(int userId, long coinNum){
        //查询在线经验信息
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userId);
        //查询游戏经验配置信息
        long maxCoin = UserGameExpConfigDao.getInstance().loadMsg();
        if(expMsg!=null && maxCoin>0){
            long dealCoin = expMsg.getCoinNum()+coinNum;//处理的游戏币
            long addExp = dealCoin/maxCoin;//添加的经验
            if(addExp>0){
                dealCoin %= maxCoin;
                expMsg.setCoinNum(dealCoin);//剩余游戏币
                long dealExp = expMsg.getExpNum()+addExp;//处理的经验
                //查询玩家属性信息
                UserAttributeMsgEntity msgEntity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                long lvExp = loadUserLvExp(msgEntity);//当前等级经验
                if (lvExp > 0 && dealExp >= lvExp) {
                    dealExp %= lvExp;
                    //提升玩家经验等级
                    userLevelUp(msgEntity);
                }
                expMsg.setExpNum(dealExp);//经验
            }
            UserOnlineExpMsgDao.getInstance().setCache(userId, expMsg);
        }
    }

    /**
     * 获取当前等级经验
     */
    private static long loadUserLvExp(UserAttributeMsgEntity msgEntity) {
        int level = msgEntity.getUserLevel();//等级
        //查询配置信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getLvExp();
    }

    /**
     * 提升玩家经验等级
     */
    private static void userLevelUp(UserAttributeMsgEntity entity) {
        entity.setUserLevel(entity.getUserLevel()+1);//等级
        UserAttributeMsgDao.getInstance().update(entity);
    }

    /**
     * 能量信息
     */
    public static UserEnergyMsg userEnergyMsg(UserAttributeMsgEntity userAttribute) {
        UserEnergyMsg msg = new UserEnergyMsg();
        msg.setCnAmt(userAttribute.getEnergyNum());//当前进度数量
        msg.setTtAmt(loadTotalEnergyNum(userAttribute.getEnergyLevel()));//总进度数量
        msg.setLfTm(loadEnergyRefreshTime(userAttribute.getChargingTime(), userAttribute.getChargingLevel()));//下次刷新时间
        return msg;
    }

    /**
     * 获取能量上限
     */
    private static long loadTotalEnergyNum(int level) {
        //查询配置信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getEnergyMax();
    }

    /**
     * 获取充能刷新时间
     */
    private static long loadEnergyRefreshTime(String chargingTime, int level) {
        //查询配置信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        long refreshTime = entity.getChargingSecond();
        return Math.max(0,refreshTime-(TimeUtil.getNowTime()-TimeUtil.strToLong(chargingTime))/1000);
    }

    /**
     * 设备奖励能量处理
     */
    public static void productAwardEnergyDeal(int userId, int productId, int getType,
            List<GeneralAwardMsg> resultAwardList) {
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                if(SuperPlayerUtil.isSuperPlayer(userId)) {
                    //查询属性信息
                    UserAttributeMsgEntity msgEntity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                    if (msgEntity.getEnergyNum() > 0) {
                        //玩家奖励列表
                        List<EnergyExchangeUserAwardEntity> awardHistoryList = new ArrayList<>();
                        //获取能量奖励
                        awardList = loadProductEnergyAward(productId, awardHistoryList);
                        if (awardList.size() > 0) {
                            //扣除能量
                            boolean flag = costUserEnergy(msgEntity);
                            if (flag) {
                                //添加设备能量奖励
                                SchedulerSample.delayed(10,
                                        new AddEnergyAwardTask(productId, userId, getType, awardList, awardHistoryList));
                            } else {
                                LogUtil.getLogger().error("玩家{}在设备{}上获得奖励，扣除能量的时候，扣除失败------", userId, productId);
                                awardList = new ArrayList<>();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
        if(awardList.size()>0){
            resultAwardList.addAll(awardList);
        }
    }

    /**
     * 获取能量奖励
     */
    private static List<GeneralAwardMsg> loadProductEnergyAward(int productId, List<EnergyExchangeUserAwardEntity> awardHistoryList) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        int configId = EnergyExchangeProductDao.getInstance().loadMsg(productId);
        if(configId>0) {
            //查询奖励列表
            List<EnergyExchangeAwardEntity> awardList = EnergyExchangeAwardDao.getInstance().loadMsg(configId);
            if(awardList.size()>0){
                awardList.forEach(entity->{
                    if(awardFlag(entity)){
                        int awardNum = StrUtil.loadInterValNum(entity.getMinNum(), entity.getMaxNum());//奖励数量
                        //返回信息
                        retList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(),
                                entity.getAwardId(), entity.getAwardImgId(), awardNum));
                        //奖励历史信息
                        awardHistoryList.add(initEnergyExchangeUserAwardEntity(entity, awardNum));
                    }
                });
            }
        }
        return retList;
    }

    /**
     * 填充能量兑换玩家奖励实体信息
     */
    private static EnergyExchangeUserAwardEntity initEnergyExchangeUserAwardEntity(
            EnergyExchangeAwardEntity awardEntity, int awardNum) {
        EnergyExchangeUserAwardEntity entity = new EnergyExchangeUserAwardEntity();
        entity.setAwardType(awardEntity.getAwardType());//奖励类型
        entity.setAwardId(awardEntity.getAwardId());//奖励ID
        entity.setMinNum(awardEntity.getMinNum());//奖励最小值
        entity.setMaxNum(awardEntity.getMaxNum());//奖励最大值
        entity.setAwardProbability(awardEntity.getAwardProbability());//中奖概率
        entity.setTotalProbability(awardEntity.getTotalProbability());//总概率
        entity.setAwardNum(awardNum);//奖励数量
        return entity;
    }

    /**
     * 是否中奖
     */
    private static boolean awardFlag(EnergyExchangeAwardEntity entity) {
        boolean flag = false;
        if(entity.getMaxTime()==0 || entity.getTriggerTime()<entity.getMaxTime()){
            if(StrUtil.isAward(entity.getAwardProbability(), entity.getTotalProbability())){
                flag = true;
            }
        }
        if(flag && entity.getMaxTime()>0){
            entity.setTriggerTime(entity.getTriggerTime()+1);//已触发次数
            EnergyExchangeAwardDao.getInstance().update(entity);
        }
        return flag;
    }

    /**
     * 扣除能量
     */
    private static boolean costUserEnergy(UserAttributeMsgEntity entity) {
        entity.setEnergyNum(entity.getEnergyNum()-1);//能量
        if(oriEnergyMax(entity.getEnergyNum(), entity.getEnergyLevel())){
            entity.setChargingTime(TimeUtil.getNowTimeStr());//重置充能时间
        }
        boolean flag = UserAttributeMsgDao.getInstance().update(entity);
        int userId = entity.getUserId();//玩家ID
        if(flag && UserOnlineUtil.isOnline(userId)) {
            //刷新能量
            UserNoticePushUtil.userEnergyMsg(userId, -1);
        }
        return flag;
    }

    /**
     * 是否能量上限
     */
    private static boolean oriEnergyMax(long energyNum, int energyLevel) {
        //查询等级信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(energyLevel);
        long maxNum = entity==null?0:entity.getEnergyMax();
        return (energyNum+1)==maxNum;
    }

    /**
     * 添加能量兑换历史信息
     */
    public static void addEnergyExchangeHistory(int userId, int productId, int getType,
            List<EnergyExchangeUserAwardEntity> awardHistoryList) {
        //添加历史信息
        long historyId = EnergyExchangeUserHistoryDao.getInstance().insert(
                initEnergyExchangeUserHistoryEntity(userId, productId, getType));
        if(historyId>0){
            //替换历史ID
            for(EnergyExchangeUserAwardEntity entity : awardHistoryList){
                entity.setHistoryId(historyId);
            }
            //添加奖励信息
            EnergyExchangeUserAwardDao.getInstance().insert(awardHistoryList);
        }else{
            LogUtil.getLogger().error("添加玩家{}在设备{}能量兑换奖励的历史信息失败------", userId, productId);
        }
    }

    /**
     * 填充能量兑换玩家历史实体信息
     */
    private static EnergyExchangeUserHistoryEntity initEnergyExchangeUserHistoryEntity(int userId, int productId, int getType) {
        EnergyExchangeUserHistoryEntity entity = new EnergyExchangeUserHistoryEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setGetType(getType);//获得类型
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }
}
