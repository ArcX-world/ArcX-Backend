package avatar.util.user;

import avatar.data.user.attribute.UserAttributeMsg;
import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.UserAttributeTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.attribute.UserAttributeConfigDao;
import avatar.module.user.attribute.UserAttributeLvListDao;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.user.RefreshUserEnergyNoticeTask;
import avatar.util.LogUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家属性信息
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
     * 获取充能刷新时间
     */
    private static long loadEnergyRefreshTime(int userId, long maxNum, String chargingTime, int level) {
        long retTime = 0;//刷新时间
        if(SuperPlayerUtil.isSuperPlayer(userId)) {
            //查询配置信息
            UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
            long refreshTime = entity.getChargingSecond();
            if (entity.getEnergyMax()<maxNum) {
                return Math.max(0, refreshTime - (TimeUtil.getNowTime() - TimeUtil.strToLong(chargingTime)) / 1000);
            }
        }
        return retTime;
    }


    /**
     * 玩家属性
     */
    public static List<UserAttributeMsg> userAttributeMsg(UserAttributeMsgEntity userAttribute) {
        List<UserAttributeMsg> retList = new ArrayList<>();
        List<UserAttributeTypeEnum> typeList = UserAttributeTypeEnum.loadAll();
        if(typeList.size()>0){
            //获取等级列表
            List<Integer> levelList = UserAttributeLvListDao.getInstance().loadMsg();
            //填充返回信息
            typeList.forEach(enumMsg-> {
                int attributeType = enumMsg.getCode();//属性类型
                retList.add(initUserAttributeMsg(attributeType, userAttribute, levelList));
            });
        }
        return retList;
    }

    /**
     * 填充玩家属性信息
     */
    private static UserAttributeMsg initUserAttributeMsg(int attributeType, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList) {
        UserAttributeMsg msg = new UserAttributeMsg();
        msg.setAtbTp(attributeType);//玩家属性类型
        msg.setCsAmt(0);//商品数量（默认值）
        msg.setNxLvAmt("");//下一等级数量（默认值）
        boolean superPlayerFlag = SuperPlayerUtil.isSuperPlayer(userAttribute.getUserId());//是否超级玩家
        switch (attributeType){
            case 1:
                //经验等级
                initExpAttribute(userAttribute, levelList, msg);
                break;
            case 2:
                //能量等级
                initEnergyAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 3:
                //充能等级
                initChargingAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 4:
                //空投等级
                initAirdropAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 5:
                //幸运等级
                initLuckyAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
            case 6:
                //魅力等级
                initCharmAttribute(superPlayerFlag, userAttribute, levelList, msg);
                break;
        }
        return msg;
    }

    /**
     * 经验等级
     */
    private static void initExpAttribute(UserAttributeMsgEntity userAttribute, List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getUserLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(lv+"");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg(nxLv>userAttribute.getUserLevel()?YesOrNoEnum.NO.getCode():YesOrNoEnum.NO.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.exp());//商品类型
        if(nxLv>0){
            msg.setCsAmt(configEntity.getLvExp());//商品数量
            msg.setNxLvAmt(nxLv+"");//下一等级数量
        }
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userAttribute.getUserId());
        msg.setSdNma(expMsg.getExpNum());//当前进度分子
        msg.setSdDma(loadTotalExpNum(lv));//当前进度分母
    }

    /**
     * 获取经验总数
     */
    private static long loadTotalExpNum(int level) {
        //查询配置信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        return entity==null?0:entity.getLvExp();
    }

    /**
     * 能量等级
     */
    private static void initEnergyAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getEnergyLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getEnergyMax()+"");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        if(nxLv>0){
            //查询当前等级配置信息
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getEnergyAxc());//商品数量
            msg.setNxLvAmt(configEntity.getEnergyMax()+"");//下一等级数量
        }
        msg.setLfTm(loadEnergyRefreshTime(userAttribute.getUserId(), configEntity.getEnergyMax(),
                userAttribute.getChargingTime(), userAttribute.getChargingLevel()));//下次刷新时间
        msg.setSdNma(lv);//当前进度分子
        msg.setSdDma(userAttribute.getUserLevel());//当前进度分母
    }

    /**
     * 充能等级
     */
    private static void initChargingAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getChargingLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getChargingSecond()+"");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        if(nxLv>0){
            //查询当前等级配置信息
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getChargingAxc());//商品数量
            msg.setNxLvAmt(configEntity.getChargingSecond()+"");//下一等级数量
        }
        msg.setSdNma(lv);//当前进度分子
        msg.setSdDma(userAttribute.getUserLevel());//当前进度分母
    }

    /**
     * 空投等级
     */
    private static void initAirdropAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getAirdropLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getAirdropCoin()+"");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        if(nxLv>0){
            //查询当前等级配置信息
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getAirdropAxc());//商品数量
            msg.setNxLvAmt(configEntity.getAirdropCoin()+"");//下一等级数量
        }
        msg.setSdNma(lv);//当前进度分子
        msg.setSdDma(userAttribute.getUserLevel());//当前进度分母
    }

    /**
     * 幸运等级
     */
    private static void initLuckyAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute, List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getLuckyLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getLuckyProbability()+"%");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        if(nxLv>0){
            //查询当前等级配置信息
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getLuckyAxc());//商品数量
            msg.setNxLvAmt(configEntity.getLuckyProbability()+"%");//下一等级数量
        }
        msg.setSdNma(lv);//当前进度分子
        msg.setSdDma(userAttribute.getUserLevel());//当前进度分母
    }

    /**
     * 魅力等级
     */
    private static void initCharmAttribute(boolean superPlayerFlag, UserAttributeMsgEntity userAttribute,
            List<Integer> levelList, UserAttributeMsg msg) {
        //等级
        int lv = userAttribute.getCharmLevel();
        msg.setLv(lv);
        //查询当前等级配置信息
        UserAttributeConfigEntity configEntity = UserAttributeConfigDao.getInstance().loadMsg(lv);
        msg.setLvAmt(configEntity.getCharmAddition()+"%");//当前等级数量
        //下一等级
        int nxLv = loadNextLevel(lv, levelList);
        msg.setNxLv(nxLv);
        msg.setUpFlg((!superPlayerFlag || nxLv>userAttribute.getUserLevel())?YesOrNoEnum.NO.getCode():YesOrNoEnum.YES.getCode());//是否可升级
        msg.setCmdTp(CommodityUtil.axc());//商品类型
        if(nxLv>0){
            //查询当前等级配置信息
            configEntity = UserAttributeConfigDao.getInstance().loadMsg(nxLv);
            msg.setCsAmt(configEntity.getCharmAxc());//商品数量
            msg.setNxLvAmt(configEntity.getCharmAddition()+"%");//下一等级数量
        }
        msg.setSdNma(lv);//当前进度分子
        msg.setSdDma(userAttribute.getUserLevel());//当前进度分母
    }

    /**
     * 获取下个等级
     */
    private static int loadNextLevel(int lv, List<Integer> levelList) {
        int retLv = 0;
        if(levelList.size()>0) {
            for (int nxLv : levelList) {
                if (nxLv > lv) {
                    retLv = nxLv;
                    break;
                }
            }
        }
        return retLv;
    }

    /**
     * 是否满足升级条件
     */
    public static int checkUpgradeAttribute(int userId, int attributeType) {
        int status = ClientCode.UPGRADE_CONDITION_NOT_FIT.getCode();//升级条件不满足
        if(attributeType!=UserAttributeTypeEnum.EXP_LEVEL.getCode()) {
            if(SuperPlayerUtil.isSuperPlayer(userId)) {
                //查询玩家属性信息
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                if (entity != null) {
                    int userLevel = entity.getUserLevel();
                    //获取下一级扣除的AXC数量
                    long costAxc = attributeLevelCost(attributeType, userLevel, entity);
                    if (costAxc != -1) {
                        //扣除等级消耗AXC
                        boolean flag = UserCostLogUtil.costAttribute(userId, attributeType, costAxc);
                        if (flag) {
                            //更新属性信息
                            UserAttributeMsgDao.getInstance().update(entity);
                            status = ClientCode.SUCCESS.getCode();//成功
                        } else {
                            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
                        }
                    }
                }
            }
        }
        return status;
    }

    /**
     * 获取下一级扣除的AXC数量
     */
    private static long attributeLevelCost(int attributeType, int userLevel, UserAttributeMsgEntity entity) {
        long costAxc = -1;//扣除的AXC数量
        int level = 1;//等级
        if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
            //能量等级
            level = entity.getEnergyLevel();
        }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
            //充能等级
            level = entity.getChargingLevel();
        }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
            //空投等级
            level = entity.getAirdropLevel();
        }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
            //幸运等级
            level = entity.getLuckyLevel();
        }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
            //魅力等级
            level = entity.getCharmLevel();
        }
        if(userLevel>level){
            //获取下一等级
            int nextLevel = loadNextLevel(level, UserAttributeLvListDao.getInstance().loadMsg());
            if(nextLevel>0){
                //扣除的acx数量
                costAxc = loadLevelCost(attributeType, nextLevel);
                //更新属性等级
                if(costAxc!=-1){
                    updateNextLevel(entity, attributeType, nextLevel);
                }
            }
        }
        return costAxc;
    }

    /**
     * 更新下一等级属性
     */
    private static void updateNextLevel(UserAttributeMsgEntity entity, int attributeType, int nextLevel) {
        if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
            //能量等级
            entity.setEnergyLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
            //充能等级
            entity.setChargingLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
            //空投等级
            entity.setAirdropLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
            //幸运等级
            entity.setLuckyLevel(nextLevel);
        }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
            //魅力等级
            entity.setCharmLevel(nextLevel);
        }
    }

    /**
     * 等级扣除的acx数量
     */
    private static long loadLevelCost(int attributeType, int level) {
        long costAxc = -1;
        //查询等级配置信息
        UserAttributeConfigEntity entity = UserAttributeConfigDao.getInstance().loadMsg(level);
        if(entity!=null){
            if(attributeType==UserAttributeTypeEnum.ENERGY_LEVEL.getCode()){
                //能量等级
                costAxc = entity.getEnergyAxc();
            }else if(attributeType==UserAttributeTypeEnum.CHARGING_LEVEL.getCode()){
                //充能等级
                costAxc = entity.getChargingAxc();
            }else if(attributeType==UserAttributeTypeEnum.AIRDROP_LEVEL.getCode()){
                //空投等级
                costAxc = entity.getAirdropAxc();
            }else if(attributeType==UserAttributeTypeEnum.LUCKY_LEVEL.getCode()){
                //幸运等级
                costAxc = entity.getLuckyAxc();
            }else if(attributeType==UserAttributeTypeEnum.CHARM_LEVEL.getCode()){
                //魅力等级
                costAxc = entity.getCharmAxc();
            }
        }
        return costAxc;
    }

    /**
     * 更新充能时间
     */
    public static void updateChargingTime(int userId) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                //查询玩家属性信息
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                entity.setChargingTime(TimeUtil.getNowTimeStr());//充能时间
                UserAttributeMsgDao.getInstance().update(entity);
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 添加玩家能量
     */
    public static void addUserEnergy(int userId, int num){
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ATTRIBUTE_LOCK + "_" + userId,
                2000);
        try {
            if (lock.lock()) {
                //查询玩家属性信息
                UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
                entity.setEnergyNum(entity.getEnergyNum()+num);//能量数
                boolean flag = UserAttributeMsgDao.getInstance().update(entity);
                if(flag){
                    if(UserOnlineUtil.isOnline(userId)){
                        //刷新能量信息
                        SchedulerSample.delayed(1, new RefreshUserEnergyNoticeTask(userId, num));
                    }
                }else{
                    LogUtil.getLogger().error("添加玩家{}的{}点能量失败------", userId, num);
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            lock.unlock();
        }
    }
}
