package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargeSuperPlayerMsg;
import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerConfigEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerOrderEntity;
import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.enumMsg.basic.recharge.PayStatusEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.superPlayer.*;
import avatar.task.recharge.AddSuperPlayerAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;

import java.util.List;

/**
 * 超级玩家工具类
 */
public class SuperPlayerUtil {
    /**
     * 填充超级玩家玩家实体信息
     */
    public static SuperPlayerUserMsgEntity initSuperPlayerUserMsgEntity(int userId) {
        SuperPlayerUserMsgEntity entity = new SuperPlayerUserMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setEffectTime("");//有效时间
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 是否超级玩家
     */
    public static boolean isSuperPlayer(int userId) {
        boolean flag = true;
        //查询玩家信息
        SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
        if(TimeUtil.getNowTime()>TimeUtil.strToLong(entity.getEffectTime())){
            //重置超级玩家缓存
            SuperPlayerUserListDao.getInstance().removeCache();
            flag = false;
        }
        return flag;
    }

    /**
     * 开通超级玩家
     * 如果之前不是超级玩家，需要更新充能时间
     */
    public static int openSuperPlayer(int userId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();//成功
        boolean superPlayerFlag = SuperPlayerUtil.isSuperPlayer(userId);//是否超级玩家
        //扣除超级玩家消耗
        boolean flag = UserCostLogUtil.costSuperPlayer(userId);
        if(flag) {
            //添加订单
            SuperPlayerOrderDao.getInstance().insert(initSuperPlayerOrderEntity(userId, PayTypeEnum.USDT.getCode()));
            //添加超级玩家时间
            addSuperUserTime(userId);
            //添加奖励
            addSuperPlayerAward(userId, retList);
            //更新充能时间
            if (ParamsUtil.isSuccess(status) && !superPlayerFlag) {
                UserAttributeUtil.updateChargingTime(userId);
            }
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        }
        return status;
    }

    /**
     * 填充超级玩家订单实体信息
     */
    private static SuperPlayerOrderEntity initSuperPlayerOrderEntity(int userId, int payType) {
        SuperPlayerOrderEntity entity = new SuperPlayerOrderEntity();
        entity.setUserId(userId);//玩家id
        entity.setOrderSn(RechargeConfigMsg.superPlayerPrefix+ StrUtil.getOrderIdByUUId());//订单号
        entity.setRechargeId("");//平台交易号
        entity.setPayType(payType);//支付类型
        entity.setPrice(loadPrice());//价格
        entity.setStatus(RechargeUtil.isDirectPayType(payType)?PayStatusEnum.ALREADY_PAY.getCode():
                PayStatusEnum.NO_PAY.getCode());//状态
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//结束时间
        return entity;
    }

    /**
     * 添加超级玩家奖励
     */
    private static void addSuperPlayerAward(int userId, List<GeneralAwardMsg> retList) {
        List<SuperPlayerAwardEntity> list = SuperPlayerAwardDao.getInstance().loadMsg();
        if(list.size()>0){
            //添加奖励信息
            SchedulerSample.delayed(1, new AddSuperPlayerAwardTask(userId, list));
            //添加返回信息
            list.forEach(entity-> retList.add(AwardUtil.initGeneralAwardMsg(entity.getAwardType(), entity.getAwardImgId(),
                    entity.getAwardNum())));
        }
    }

    /**
     * 添加超级玩家时间
     */
    private static void addSuperUserTime(int userId) {
        int addTime = loadSuperTime();//添加时间
        if(addTime>0) {
            //查询信息
            SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
            if (TimeUtil.getNowTime() > TimeUtil.strToLong(entity.getEffectTime())) {
                entity.setEffectTime(TimeUtil.getAfterNHour(TimeUtil.getNowTimeStr(), addTime));
            }else{
                entity.setEffectTime(TimeUtil.getAfterNHour(entity.getEffectTime(), addTime));
            }
            SuperPlayerUserDao.getInstance().update(entity);
        }
    }

    /**
     * 获取超级玩家添加时间
     */
    private static int loadSuperTime() {
        //查询信息
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getEffectDay();
    }

    /**
     * 超级玩家价格
     */
    public static int loadPrice() {
        //查询信息
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        return entity==null?0:entity.getPrice();
    }

    /**
     * 超级玩家信息
     */
    public static RechargeSuperPlayerMsg rechargeSuperPlayerMsg() {
        RechargeSuperPlayerMsg msg = new RechargeSuperPlayerMsg();
        //查询信息
        SuperPlayerConfigEntity entity = SuperPlayerConfigDao.getInstance().loadMsg();
        msg.setPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));//图片
        msg.setUsdtAmt(entity==null?0:entity.getPrice());//图片
        return msg;
    }
}
