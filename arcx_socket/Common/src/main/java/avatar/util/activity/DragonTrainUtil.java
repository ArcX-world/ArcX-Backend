package avatar.util.activity;

import avatar.data.activity.dragonTrain.DragonTrainAwardIndexMsg;
import avatar.data.activity.dragonTrain.DragonTrainAwardMsg;
import avatar.data.activity.dragonTrain.DragonTrainAwardPushMsg;
import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.activity.dragonTrain.info.DragonTrainConfigMsgEntity;
import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainBallUserGetHistoryEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainTriggerAwardMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserTriggerMsgEntity;
import avatar.global.code.basicConfig.ActivityConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.activity.dragonTrain.info.DragonTrainConfigMsgDao;
import avatar.module.activity.dragonTrain.info.DragonTrainWheelAwardIconMsgDao;
import avatar.module.activity.dragonTrain.info.DragonTrainWheelIconMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainBallUserGetHistoryDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserTriggerAwardMsgDao;
import avatar.module.activity.dragonTrain.user.DragonTrainUserTriggerMsgDao;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.basic.general.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserNoticePushUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  龙珠玛丽机工具类
 */
public class DragonTrainUtil {
    /**
     * 填充龙珠玛丽机玩家信息
     */
    public static DragonTrainUserMsgEntity initDragonTrainUserMsgEntity(int userId) {
        DragonTrainUserMsgEntity entity = new DragonTrainUserMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setDragonNum(0);//龙珠数量
        entity.setTriggerTime(0);//触发次数
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }


    /**
     * 添加玩家龙珠信息
     */
    public static void addUserDragon(int userId, int productId){
        //查询玩家龙珠玛丽机信息
        DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            JSONObject dataJson = new JSONObject();//推送前端的参数信息
            int currentDragonNum = entity.getDragonNum()+1;//当前龙珠数
            //更新龙珠玛丽机信息
            boolean isTrigger = updateDragonTrainUserMsg(entity);
            //获取玩家龙珠中奖游戏币数
            int awardCoin = loadDragonBounsAwardCoin(userId);
            if(awardCoin>0) {
                //添加玩家中奖游戏币数
                UserCostLogUtil.addDragonAward(userId, productId, awardCoin);
            }
            //添加龙珠玛丽机龙珠玩家获得历史
            DragonTrainBallUserGetHistoryDao.getInstance().insert(initDragonTrainBallUserGetHistoryEntity(
                    userId, productId, currentDragonNum, awardCoin,
                    isTrigger? YesOrNoEnum.YES.getCode():YesOrNoEnum.NO.getCode()));
            List<GeneralAwardMsg> dragonAwardList = new ArrayList<>();
            dataJson.put("cnbAmt", currentDragonNum);//当前龙珠数量
            //能量处理
            UserAttributeUtil.productAwardEnergyDeal(userId, productId, EnergyExchangeGetTypeEnum.DRAGON_BALL.getCode(),
                    dragonAwardList);
            //填充奖励游戏币
            addAwardCoin(awardCoin, dragonAwardList);
            dataJson.put("awdTbln", dragonAwardList);//返回信息
            if(isTrigger) {
                //中奖龙珠玛丽机处理
                //获取中奖列表
                List<DragonTrainAwardMsg> awardList = loadWheelAwardMsg(userId, productId);
                //添加触发信息
                addTriggerMsg(userId, productId, awardList);
                //添加玩家对应的奖励
                addUserTrainAwardList(userId, productId, awardList);
                //填充推送的龙珠玛丽机中奖信息
                fillDragonTrainAwardMsg(dataJson, awardList);
            }
            //推送前端返回信息
            UserNoticePushUtil.pushDragonAwardMsg(userId, dataJson);
        }else{
            LogUtil.getLogger().info("添加玩家{}自研设备龙珠信息的时候，查询不到玩家龙珠玛丽机信息-------", userId);
        }
    }

    /**
     * 填充奖励游戏币
     */
    private static void addAwardCoin(int awardCoin, List<GeneralAwardMsg> awardList) {
        //查询配置信息
        DragonTrainConfigMsgEntity entity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        awardList.add(AwardUtil.initGeneralAwardMsg(CommodityTypeEnum.GOLD_COIN.getCode(),
                0, entity.getAwardImgId(), awardCoin));
    }

    /**
     * 更新玩家龙珠玛丽机信息
     */
    private static boolean updateDragonTrainUserMsg(DragonTrainUserMsgEntity entity) {
        boolean isTrigger = false;//是否触发：否
        int triggerNum = ActivityConfigMsg.triggerBallNum;//触发龙珠数量
        int dragonNum = entity.getDragonNum();//龙珠数量
        if(dragonNum+1>=triggerNum){
            //触发了
            entity.setDragonNum(dragonNum+1-triggerNum);//龙珠数
            entity.setTriggerTime(entity.getTriggerTime()+1);//触发次数
            isTrigger = true;
        }else{
            //尚未触发
            entity.setDragonNum(dragonNum+1);//龙珠数
        }
        boolean flag = DragonTrainUserMsgDao.getInstance().update(entity);
        if(!flag){
            LogUtil.getLogger().info("更新玩家{}自研设备龙珠玛丽机信息的时候，更新失败--------", entity.getUserId());
        }
        return isTrigger;
    }

    /**
     * 获取龙珠中奖游戏币
     */
    private static int loadDragonBounsAwardCoin(int userId) {
        int awardCoin = 0;//奖励游戏币
        //查询自研龙珠配置信息
        DragonTrainConfigMsgEntity entity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        if(entity!=null){
            awardCoin = StrUtil.loadInterValNum(entity.getDragonMinNum(), entity.getDragonMaxNum());
        }else {
            LogUtil.getLogger().info("添加玩家{}自研龙珠中奖游戏币的时候，查询不到龙珠玛丽机配置信息-------", userId);
        }
        return awardCoin;
    }

    /**
     * 填充获得历史信息
     */
    private static DragonTrainBallUserGetHistoryEntity initDragonTrainBallUserGetHistoryEntity(
            int userId, int productId, int currentNum, int awardCoin, int isTrigger){
        DragonTrainBallUserGetHistoryEntity entity = new DragonTrainBallUserGetHistoryEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setCurrentNum(currentNum);//当前数量
        entity.setAwardCoin(awardCoin);//奖励游戏币数
        entity.setIsTrigger(isTrigger);//是否触发玛丽机
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 获取转轮中奖信息
     */
    private static List<DragonTrainAwardMsg> loadWheelAwardMsg(int userId, int productId){
        List<DragonTrainAwardMsg> awardList = new ArrayList<>();
        //查询转轮图标信息列表
        List<DragonTrainWheelIconMsgEntity> iconList = DragonTrainWheelAwardIconMsgDao.getInstance().loadMsg();
        //查询龙珠玛丽机配置信息
        DragonTrainConfigMsgEntity configMsgEntity = DragonTrainConfigMsgDao.getInstance().loadMsg();
        if(configMsgEntity!=null && iconList.size()>0){
            int awardNum = 0;//中奖数量
            fillAwardMsg(awardNum, iconList, configMsgEntity, awardList);
            //处理返回信息
            awardList = new ArrayList<>(dealRetAwardMsg(awardList));
        }else{
            LogUtil.getLogger().info("玩家{}在设备{}上中了龙珠玛丽机，获取转轮中奖信息的时候，查询不到玛丽机配置信息或者" +
                    "查询不到玛丽机转轮图标信息-----", userId, productId);
        }
        return awardList;
    }

    /**
     * 中奖数量
     */
    private static void fillAwardMsg(int awardNum, List<DragonTrainWheelIconMsgEntity> iconList,
            DragonTrainConfigMsgEntity configMsgEntity, List<DragonTrainAwardMsg> awardList) {
        LogUtil.getLogger().info("第{}次获取龙珠玛丽机中奖信息--------", awardNum+1);
        boolean repeatFlag = configMsgEntity.getIsRepeatAward()==YesOrNoEnum.YES.getCode();//是否奖励可重复
        boolean againAwardFlag = canAwardAgain(configMsgEntity, awardList);//是否能中again图标
        //处理可选的中奖列
        List<DragonTrainWheelIconMsgEntity> selectIconList = dealSelectIconList(iconList, awardList, repeatFlag,
                againAwardFlag);
        //判断是否全中了
        boolean allAward = isAllAward(selectIconList);//全中了
        if(!allAward) {
            //填充中奖信息
            addAwardMsg(awardNum, selectIconList, awardList);
            if(awardList.get(awardList.size()-1).getWheelIconMsg().
                    getCommodityType()!= CommodityTypeEnum.AGAIN.getCode()) {
                awardNum += 1;
            }
        }
        if(!allAward && awardNum<configMsgEntity.getAwardNum()){
            fillAwardMsg(awardNum, iconList, configMsgEntity, awardList);
        }
    }

    /**
     * 是否能中again图标
     */
    private static boolean canAwardAgain(DragonTrainConfigMsgEntity configMsgEntity, List<DragonTrainAwardMsg> awardList) {
        int againMaxTime = configMsgEntity.getAgainTime();//能中again的上限次数
        int againAwardTime = 0;//中again的次数
        for(DragonTrainAwardMsg msg : awardList){
            if(msg.getWheelIconMsg().getCommodityType()==CommodityTypeEnum.AGAIN.getCode()){
                againAwardTime += 1;
            }
        }
        return againMaxTime>againAwardTime;
    }

    /**
     * 处理可选的中奖列
     */
    private static List<DragonTrainWheelIconMsgEntity> dealSelectIconList(
            List<DragonTrainWheelIconMsgEntity> iconList, List<DragonTrainAwardMsg> awardList,
            boolean repeatFlag, boolean againAwardFlag) {
        List<DragonTrainWheelIconMsgEntity> retList = new ArrayList<>();
        if(awardList.size()==0){
            //没选过
            retList = new ArrayList<>(iconList);
        }else{
            //有中奖信息了
            for(DragonTrainWheelIconMsgEntity entity : iconList){
                if(isNoSelectAward(entity, awardList, repeatFlag, againAwardFlag)){
                    retList.add(entity);
                }
            }
        }
        return retList;
    }

    /**
     * 是否未中过的奖励
     */
    private static boolean isNoSelectAward(DragonTrainWheelIconMsgEntity entity,
            List<DragonTrainAwardMsg> awardList, boolean repeatFlag, boolean againAwardFlag) {
        boolean flag = true;//未中过的奖励
        if(entity.getCommodityType()==CommodityTypeEnum.AGAIN.getCode() && !againAwardFlag){
            //again，但是已经不允许重复
            flag = false;
        }else {
            for (DragonTrainAwardMsg msg : awardList) {
                DragonTrainWheelIconMsgEntity wheelIconMsg = msg.getWheelIconMsg();//转轮图标信息
                if (entity.getId() == msg.getWheelIconMsg().getId() || (
                        wheelIconMsg.getCommodityType()!=CommodityTypeEnum.AGAIN.getCode() &&
                                wheelIconMsg.getCommodityType() == entity.getCommodityType() &&
                                wheelIconMsg.getGiftId() == entity.getGiftId() && !repeatFlag)) {
                    flag = false;//中过了
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 是否所有奖项都中过了
     */
    private static boolean isAllAward(List<DragonTrainWheelIconMsgEntity> selectIconList) {
        boolean flag = true;
        if(selectIconList.size()>0) {
            for (DragonTrainWheelIconMsgEntity entity : selectIconList) {
                if (entity.getCommodityType() != CommodityTypeEnum.AGAIN.getCode()) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 填充中奖信息
     */
    private static void addAwardMsg(int awardNum, List<DragonTrainWheelIconMsgEntity> selectIconList,
            List<DragonTrainAwardMsg> awardList) {
        DragonTrainWheelIconMsgEntity firstIcon = selectIconList.get(0);//首个图标
        DragonTrainWheelIconMsgEntity awardIcon = null;//中奖图标
        Collections.shuffle(selectIconList);//随机排序
        //判断图标是否中奖了，是则直接添加
        for(DragonTrainWheelIconMsgEntity iconMsg : selectIconList){
            if(isIconAward(iconMsg)){
                awardIcon = iconMsg;
                break;
            }
        }
        //没有任何图标中奖，直接拿最高几率的
        if(awardIcon==null){
            awardIcon = firstIcon;
        }
        DragonTrainAwardMsg awardMsg = initDragonTrainAwardMsg(awardIcon);//中奖信息
        awardList.add(awardMsg);//添加中奖信息
        LogUtil.getLogger().info("第{}次获取龙珠玛丽机中奖结果信息：中奖信息{}，数量{}--------", awardNum+1,
                CommodityUtil.loadAwardName(awardIcon.getCommodityType(), awardIcon.getGiftId()),
                awardMsg.getResultAwardNum());
    }

    /**
     * 是否图标中奖
     */
    private static boolean isIconAward(DragonTrainWheelIconMsgEntity iconMsg) {
        boolean flag = false;
        int awardPro = iconMsg.getAwardProbability();//中奖几率
        int totalPro = iconMsg.getTotalProbability();//总几率
        if(totalPro>=awardPro && totalPro>0 && awardPro>0){
            flag = StrUtil.isAward(awardPro, totalPro);
        }
        return flag;
    }

    /**
     * 填充中奖信息
     */
    private static DragonTrainAwardMsg initDragonTrainAwardMsg(DragonTrainWheelIconMsgEntity awardIcon) {
        DragonTrainAwardMsg msg = new DragonTrainAwardMsg();
        msg.setResultAwardNum(StrUtil.loadInterValNum(awardIcon.getAwardMinNum(), awardIcon.getAwardMaxNum()));//最终奖励值
        msg.setWheelIconMsg(awardIcon);//转轮图标信息
        return msg;
    }

    /**
     * 处理返回信息，如果最后一个是再来一次，去掉
     */
    private static List<DragonTrainAwardMsg> dealRetAwardMsg(List<DragonTrainAwardMsg> awardList) {
        List<DragonTrainAwardMsg> retList = new ArrayList<>();
        int againNum = 0;//需要删除的again次数
        for(int i=awardList.size()-1;i>=0;i--){
            if(awardList.get(i).getWheelIconMsg().getCommodityType()==CommodityTypeEnum.AGAIN.getCode()){
                againNum += 1;
            }else{
                break;
            }
        }
        if(againNum>0) {
            if (awardList.size() > againNum) {
                for (int i = 0; i < (awardList.size() - againNum); i++){
                    retList.add(awardList.get(i));
                }
            }
        }else{
            retList = new ArrayList<>(awardList);
        }
        return retList;
    }

    /**
     * 添加龙珠玛丽机玩家触发信息
     */
    private static void addTriggerMsg(int userId, int productId, List<DragonTrainAwardMsg> awardList) {
        DragonTrainUserTriggerMsgEntity triggerMsgEntity = DragonTrainUserTriggerMsgDao.getInstance()
                .insert(initDragonTrainUserTriggerMsgEntity(userId, productId));
        if(triggerMsgEntity!=null){
            int triggerId = triggerMsgEntity.getId();//触发ID
            if(awardList.size()>0){
                boolean flag = DragonTrainUserTriggerAwardMsgDao.getInstance().insert(
                        initDragonTrainTriggerAwardMsgEntity(triggerId, awardList));
                if(!flag){
                    LogUtil.getLogger().info("添加玩家{}在设备{}上的龙珠玛丽机玩家触发信息对应的奖励信息失败--------", userId, productId);
                }
            }
        }else{
            LogUtil.getLogger().info("添加玩家{}在设备{}上的龙珠玛丽机玩家触发信息失败--------", userId, productId);
        }
    }

    /**
     * 填充玛丽机玩家触发信息
     */
    private static DragonTrainUserTriggerMsgEntity initDragonTrainUserTriggerMsgEntity(int userId, int productId){
        DragonTrainUserTriggerMsgEntity entity = new DragonTrainUserTriggerMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 填充玛丽机触发中奖信息
     */
    private static List<DragonTrainTriggerAwardMsgEntity> initDragonTrainTriggerAwardMsgEntity(
            int triggerId, List<DragonTrainAwardMsg> awardList){
        List<DragonTrainTriggerAwardMsgEntity> retList = new ArrayList<>();
        awardList.forEach(msg->{
            DragonTrainTriggerAwardMsgEntity entity = new DragonTrainTriggerAwardMsgEntity();
            DragonTrainWheelIconMsgEntity wheelIconMsg = msg.getWheelIconMsg();//转轮图标信息
            entity.setTriggerId(triggerId);//触发ID
            entity.setCommodityType(wheelIconMsg.getCommodityType());//奖励类型
            entity.setGiftId(wheelIconMsg.getGiftId());//奖励ID
            entity.setResultAwardNum(msg.getResultAwardNum());//最终奖励值
            entity.setAwardMinNum(wheelIconMsg.getAwardMinNum());//奖励最小值
            entity.setAwardMaxNum(wheelIconMsg.getAwardMaxNum());//奖励最大值
            entity.setAwardProbability(wheelIconMsg.getAwardProbability());//中奖几率
            entity.setTotalProbability(wheelIconMsg.getTotalProbability());//总几率
            retList.add(entity);
        });
        return retList;
    }

    /**
     * 添加玩家玛丽机对应的奖励
     */
    private static void addUserTrainAwardList(int userId, int productId, List<DragonTrainAwardMsg> awardList) {
        if(awardList.size()>0){
            awardList.forEach(awardMsg-> addUserTrainAwardMsg(userId, productId, awardMsg));
        }
    }

    /**
     * 添加玩家玛丽机奖励信息
     */
    private static void addUserTrainAwardMsg(int userId, int productId, DragonTrainAwardMsg awardMsg) {
        int awardType = awardMsg.getWheelIconMsg().getCommodityType();//奖励类型
        int awardId = awardMsg.getWheelIconMsg().getGiftId();//奖励ID
        int awardNum = awardMsg.getResultAwardNum();//奖励数量
        if(awardNum>0) {
            if (CommodityUtil.normalFlag(awardType)) {
                //普通商品
                UserCostLogUtil.dragonTrainAward(userId, productId, awardNum, awardType);
            } else if (awardType==CommodityTypeEnum.PROPERTY.getCode()){
                //道具
                UserCostLogUtil.dragonTrainProperty(userId, awardId, awardNum);
            }
        }
    }

    /**
     * 填充推送的龙珠玛丽机中奖信息
     */
    private static void fillDragonTrainAwardMsg(JSONObject dataJson, List<DragonTrainAwardMsg> awardList) {
        DragonTrainAwardPushMsg dragonTrainAwardMsg = new DragonTrainAwardPushMsg();
        dragonTrainAwardMsg.setIcTbln(dragonTrainIconList());//图标列表
        dragonTrainAwardMsg.setIcAwdTbln(dragonTrainIconAwardIndexList(awardList));//中奖图标列表
        dragonTrainAwardMsg.setAwdTbln(initPushAwardList(awardList));//最终奖励信息
        dataJson.put("dgTnTbln", dragonTrainAwardMsg);//龙珠玛丽机中奖信息
    }

    /**
     * 龙珠玛丽机图标列表
     */
    private static List<String> dragonTrainIconList() {
        List<String> retList = new ArrayList<>();
        //查询图标列表信息
        List<DragonTrainWheelIconMsgEntity> list = DragonTrainWheelIconMsgDao.getInstance().loadMsg();
        if(list.size()>0){
            list.forEach(entity->{
                String awardImg = ImgUtil.loadAwardImg(entity.getAwardImgId());
                if(!StrUtil.checkEmpty(awardImg)){
                    retList.add(MediaUtil.getMediaUrl(awardImg));
                }
            });
        }
        return retList;
    }

    /**
     * 龙珠玛丽机中奖图标列表
     */
    private static List<DragonTrainAwardIndexMsg> dragonTrainIconAwardIndexList(List<DragonTrainAwardMsg> awardList) {
        List<DragonTrainAwardIndexMsg> retList = new ArrayList<>();
        awardList.forEach(awardMsg->{
            DragonTrainAwardIndexMsg msg = new DragonTrainAwardIndexMsg();
            msg.setCmdTp(awardMsg.getWheelIconMsg().getCommodityType());//奖励类型
            msg.setAwdInx(awardMsg.getWheelIconMsg().getId()-1);//中奖图标位置
            retList.add(msg);
        });
        return retList;
    }

    /**
     * 填充推送的奖励列表
     */
    private static List<GeneralAwardMsg> initPushAwardList(List<DragonTrainAwardMsg> awardList) {
        List<GeneralAwardMsg> retList = new ArrayList<>();
        awardList.forEach(awardMsg->{
            int awardType = awardMsg.getWheelIconMsg().getCommodityType();//奖励类型
            int awardId = awardMsg.getWheelIconMsg().getGiftId();//奖励ID
            int imgId = awardMsg.getWheelIconMsg().getAwardImgId();//奖励图片ID
            if(awardType!=CommodityTypeEnum.AGAIN.getCode()) {
                //过滤再来一次
                //如果相同的商品类型，则叠加
                boolean addFlag = false;
                if(retList.size()>0){
                    for(GeneralAwardMsg msg : retList){
                        if (msg.getCmdTp() == awardType && msg.getCmdId() == awardId) {
                            msg.setAwdAmt(msg.getAwdAmt() + awardMsg.getResultAwardNum());//奖票数量
                            addFlag = true;
                            break;
                        }
                    }
                }
                if(!addFlag){
                    retList.add(AwardUtil.initGeneralAwardMsg(awardType, awardId, imgId, awardMsg.getResultAwardNum()));
                }
            }
        });
        return retList;
    }

}
