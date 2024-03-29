package avatar.util.activity;

import avatar.data.activity.welfare.WelfareSignAwardMsg;
import avatar.data.basic.award.GeneralAwardMsg;
import avatar.entity.activity.sign.info.WelfareSignAwardEntity;
import avatar.entity.activity.sign.user.WelfareSignUserMsgEntity;
import avatar.global.basicConfig.basic.ActivityConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.activity.sign.WelfareSignAwardDao;
import avatar.module.activity.sign.WelfareSignUserDao;
import avatar.util.LogUtil;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.log.CostUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 福利工具类
 */
public class WelfareUtil {
    /**
     * 处理玩家签到信息
     */
    public static void dealUserSignMsg(WelfareSignUserMsgEntity entity) {
        long recentlySignTime = StrUtil.checkEmpty(entity.getSignTime())?0:
                TimeUtil.strToLong(entity.getSignTime());//最近签到时间
        //判断更新缓存信息
        if(!TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))){
            //不是当天的时间，需要更新
            //判断是否超过1天，是则将连续更新置为0，如果，连续签到为7，也需要更新为0
            boolean updateFlag = false;//是否需要更新
            if(entity.getContinueSignDay()>=ActivityConfigMsg.welfareSignMaxDay){
                entity.setContinueSignDay(0);//连续签到时间
                updateFlag = true;
            }else{
                long time1 = TimeUtil.strToLong(TimeUtil.getNowDay()+" 00:00:00");
                long time2 = TimeUtil.strToLong(TimeUtil.longToDay(recentlySignTime)+" 00:00:00");
                if((time1-time2)>24*60*60*1000){
                    entity.setContinueSignDay(0);//连续签到时间
                    updateFlag = true;
                }
            }
            //更新缓存信息
            if(updateFlag){
                WelfareSignUserDao.getInstance().update(entity);
            }
        }
    }

    /**
     * 填充福利签到玩家信息实体
     */
    public static WelfareSignUserMsgEntity initWelfareSignUserMsgEntity(int userId) {
        WelfareSignUserMsgEntity entity = new WelfareSignUserMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setSignTime("");//签到日期
        entity.setContinueSignDay(0);//连续签到天数
        entity.setSumSignDay(0);//累积签到天数
        return entity;
    }

    /**
     * 签到信息
     */
    public static void signMsg(int userId, Map<String, Object> dataMap) {
        //查询玩家福利签到信息
        WelfareSignUserMsgEntity userMsgEntity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(userMsgEntity!=null) {
            //最近一次签到时间
            long recentlySignTime = 0;//最近签到时间
            if (!StrUtil.checkEmpty(userMsgEntity.getSignTime())) {
                //最近一次签到时间
                recentlySignTime = TimeUtil.strToLong(userMsgEntity.getSignTime());
            }
            //今天是否签到
            boolean todaySign = false;
            if (recentlySignTime > 0 && TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))) {
                todaySign = true;
            }
            //今天是连续第几天签到
            int continueSignDay = todaySign ? userMsgEntity.getContinueSignDay()
                    : (userMsgEntity.getContinueSignDay() + 1);//连续签到日
            dataMap.put("awdAmt", loadSignDayAwardCoin());//奖励游戏币
            dataMap.put("nxTm", todaySign ?
                    (TimeUtil.getZeroLongTime(TimeUtil.getNextDay(TimeUtil.getNowTimeStr())) -
                            TimeUtil.getNowTime()) / 1000 : 0);////下一次签到剩余时间（单位秒）
            dataMap.put("snTbln", loadSignList(continueSignDay, todaySign));//签到信息列表
            dataMap.put("cnDy", userMsgEntity.getContinueSignDay());//当前签到天数
            dataMap.put("ttDy", ActivityConfigMsg.welfareSignMaxDay);//总签到天数
        }else{
            LogUtil.getLogger().info("玩家{}获取福利签到奖励窗口信息的时候，查询不到福利签到奖励玩家信息--------", userId);
        }
    }

    /**
     * 获取当前签到的游戏币
     */
    private static int loadSignDayAwardCoin(){
        int awardCoin = 0;//奖励游戏币
        List<Integer> dayList = WelfareSignAwardDao.getInstance().loadAllDay();
        if(dayList.size()>0) {
            for(int day : dayList){
                //查询当天签到奖励信息
                List<WelfareSignAwardEntity> list = loadSignDayAward(day);
                if (list != null && list.size() > 0) {
                    for (WelfareSignAwardEntity entity : list) {
                        if (entity.getAwardType() ==CommodityUtil.gold()) {
                            awardCoin += entity.getAwardNum();
                        }
                    }
                }
            }
        }
        return awardCoin;
    }

    /**
     * 查询签到日期奖励
     */
    private static List<WelfareSignAwardEntity> loadSignDayAward(int day){
        ConcurrentHashMap<Integer, List<WelfareSignAwardEntity>> map = WelfareSignAwardDao.getInstance().loadAll();
        return map.getOrDefault(day, null);
    }

    /**
     * 获取福利签到信息列表
     */
    private static List<WelfareSignAwardMsg> loadSignList(int continueSignDay, boolean todaySign) {
        List<WelfareSignAwardMsg> retList = new ArrayList<>();
        //查询所有天数
        List<Integer> dayList = WelfareSignAwardDao.getInstance().loadAllDay();
        if(dayList.size()>0){
            dayList.forEach(day->{
                //查询签到日期奖励
                List<WelfareSignAwardEntity> list = loadSignDayAward(day);
                retList.add(initWelfareSignAwardMsg(list, day, continueSignDay, todaySign));
            });
        }
        return retList;
    }

    /**
     * 填充福利签到奖励信息
     */
    private static WelfareSignAwardMsg initWelfareSignAwardMsg(List<WelfareSignAwardEntity> list,
            int day, int continueSignDay, boolean todaySign) {
        WelfareSignAwardMsg msg = new WelfareSignAwardMsg();
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        if(list.size()>0){
            list.forEach(entity-> awardList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(),
                    entity.getAwardImgId(), entity.getAwardNum())));
        }
        int isSign = YesOrNoEnum.NO.getCode();//是否签到：否
        if(day<continueSignDay || (day==continueSignDay && todaySign)) {
            isSign = YesOrNoEnum.YES.getCode();//是否签到：是
        }
        msg.setSnFlag(isSign);//是否已经签到
        msg.setEarnArr(awardList);//奖励列表
        return msg;
    }

    /**
     * 领取签到奖励
     */
    public static int receiveSignBonus(int userId, List<GeneralAwardMsg> awardList) {
        int status = ClientCode.SUCCESS.getCode();//成功
        //查询玩家福利签到信息
        WelfareSignUserMsgEntity userMsgEntity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(userMsgEntity!=null) {
            //最近一次签到时间
            long recentlySignTime = 0;//最近签到时间
            if (!StrUtil.checkEmpty(userMsgEntity.getSignTime())) {
                //最近一次签到时间
                recentlySignTime = TimeUtil.strToLong(userMsgEntity.getSignTime());
            }
            //今天是否签到
            boolean todaySign = false;
            if (recentlySignTime > 0 && TimeUtil.getNowDay().equals(TimeUtil.longToDay(recentlySignTime))) {
                todaySign = true;
            }
            //今天是连续第几天签到
            int continueSignDay = todaySign ? userMsgEntity.getContinueSignDay()
                    : (userMsgEntity.getContinueSignDay() + 1);//连续签到日
            if(!todaySign){
                //更新签到信息
                boolean flag = updateSignMsg(userMsgEntity);
                if(flag) {
                    //添加奖励信息
                    dealSignBonus(userId, continueSignDay, awardList);
                }else {
                    LogUtil.getLogger().info("更新玩家{}福利签到奖励信息失败-----", userId);
                    status = ClientCode.FAIL.getCode();//失败
                }
            }else{
                status = ClientCode.NO_BONUS_TIME.getCode();//无奖励次数
            }
        }else{
            LogUtil.getLogger().info("玩家{}领取福利签到奖励的时候，查询不到福利签到奖励玩家信息--------", userId);
            status = ClientCode.FAIL.getCode();//失败
        }
        return status;
    }

    /**
     * 添加玩家签到奖励
     */
    private static void dealSignBonus(int userId, int day, List<GeneralAwardMsg> awardList) {
        //查询奖励信息
        List<WelfareSignAwardEntity> list = loadSignDayAward(day);
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                int commodityType = entity.getAwardId();//商品类型
                int awardNum = entity.getAwardNum();//奖励数量
                if(CommodityUtil.normalFlag(commodityType)){
                    //普通奖励
                    CostUtil.addWelfareSignCommodity(userId, commodityType, awardNum);
                }
                //填充返回奖励信息
                awardList.add(AwardUtil.initGeneralAwardMsg(commodityType, entity.getAwardImgId(), awardNum));
            });
        }
    }

    /**
     * 更新福利签到信息
     */
    private static boolean updateSignMsg(WelfareSignUserMsgEntity entity) {
        entity.setSignTime(TimeUtil.getNowTimeStr());//签到时间
        entity.setContinueSignDay(entity.getContinueSignDay()+1);//连续签到天数
        entity.setSumSignDay(entity.getSumSignDay()+1);//总签到天数
        //更新
        return WelfareSignUserDao.getInstance().update(entity);
    }

    /**
     * 重置玩家签到信息
     */
    public static void resetUserSignMsg(int userId, int continueDay) {
        //查询福利签到玩家信息
        WelfareSignUserMsgEntity entity = WelfareSignUserDao.getInstance().loadByUserId(userId);
        if(entity!=null){
            if(continueDay>0){
                if(continueDay>ActivityConfigMsg.welfareSignMaxDay){
                    continueDay = ActivityConfigMsg.welfareSignMaxDay;//最大7天
                }
                entity.setSignTime(TimeUtil.getBeforeNHour(TimeUtil.getNowTimeStr(), 24));//获取24小时前的时间
            }else{
                entity.setSignTime("");//重置签到时间
                continueDay = 0;
            }
            entity.setContinueSignDay(continueDay);
            //更新
            WelfareSignUserDao.getInstance().update(entity);
        }
    }


}
