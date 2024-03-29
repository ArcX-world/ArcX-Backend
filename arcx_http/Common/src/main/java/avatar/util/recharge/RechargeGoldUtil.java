package avatar.util.recharge;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.recharge.RechargeCoinMsg;
import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.entity.recharge.gold.RechargeGoldOrderEntity;
import avatar.global.basicConfig.basic.RechargeConfigMsg;
import avatar.global.enumMsg.basic.recharge.PayStatusEnum;
import avatar.global.enumMsg.basic.recharge.PayTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.recharge.gold.GoldUsdtConfigDao;
import avatar.module.recharge.gold.RechargeGoldInfoDao;
import avatar.module.recharge.gold.RechargeGoldListDao;
import avatar.module.recharge.gold.RechargeGoldOrderDao;
import avatar.task.recharge.AddOfficialRechargeCoinAwardTask;
import avatar.util.basic.AwardUtil;
import avatar.util.basic.CommodityUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.log.UserCostLogUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值金币工具类
 */
public class RechargeGoldUtil {
    /**
     * 游戏币列表
     */
    public static List<RechargeCoinMsg> loadCoinList() {
        List<RechargeCoinMsg> retList = new ArrayList<>();
        List<Integer> list = RechargeGoldListDao.getInstance().loadMsg();
        if(list.size()>0){
            list.forEach(id-> retList.add(initRechargeCoinMsg(id)));
        }
        return retList;
    }

    /**
     * 填充充值游戏币信息
     */
    private static RechargeCoinMsg initRechargeCoinMsg(int id) {
        RechargeCoinMsg msg = new RechargeCoinMsg();
        msg.setCmdId(id);//商品ID
        //查询信息
        RechargeGoldInfoEntity entity = RechargeGoldInfoDao.getInstance().loadById(id);
        if(entity!=null){
            msg.setCnAmt(goldNum(entity.getPrice()));//游戏币数量
            msg.setUsdtAmt(entity.getPrice());//USDT价格
            msg.setPct(MediaUtil.getMediaUrl(entity.getImgUrl()));//图片
        }
        return msg;
    }

    /**
     * 金币数量
     */
    public static long goldNum(int price){
        return GoldUsdtConfigDao.getInstance().loadMsg()*(long)price;
    }

    /**
     * 基础兑换数量
     */
    public static long basicGoldNum(){
        return GoldUsdtConfigDao.getInstance().loadMsg();
    }

    /**
     * 充值金币
     */
    public static int rechargeGold(int userId, int productId, int commodityId, List<GeneralAwardMsg> retList) {
        int status = ClientCode.SUCCESS.getCode();//成功
        //查询商品信息
        RechargeGoldInfoEntity goldInfoEntity = RechargeGoldInfoDao.getInstance().loadById(commodityId);
        //扣除充值金币消耗
        boolean flag = UserCostLogUtil.costOfficialRechargeGold(userId, goldInfoEntity.getPrice());
        if(flag) {
            //添加订单
            RechargeGoldOrderDao.getInstance().insert(initRechargeGoldOrderEntity(userId, productId,
                    goldInfoEntity, PayTypeEnum.USDT.getCode()));
            //添加奖励
            addRechargeGoldAward(userId, goldInfoEntity, retList);
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        }
        return status;
    }

    /**
     * 添加充值金币奖励
     */
    private static void addRechargeGoldAward(int userId, RechargeGoldInfoEntity entity,
            List<GeneralAwardMsg> retList) {
        long goldNum = goldNum(entity.getPrice());//金币数
        //添加返回信息
        retList.add(AwardUtil.initGeneralAwardMsg(CommodityUtil.gold(), MediaUtil.getMediaUrl(entity.getImgUrl()),
                goldNum));
        //添加奖励信息
        SchedulerSample.delayed(1, new AddOfficialRechargeCoinAwardTask(userId, goldNum));
    }

    /**
     * 填充充值金币订单实体信息
     */
    private static RechargeGoldOrderEntity initRechargeGoldOrderEntity(int userId, int productId,
            RechargeGoldInfoEntity goldInfoEntity, int payType) {
        RechargeGoldOrderEntity entity = new RechargeGoldOrderEntity();
        entity.setUserId(userId);//玩家id
        entity.setCommodityId(goldInfoEntity.getId());//商品ID
        entity.setCommodityNum(goldNum(goldInfoEntity.getPrice()));//商品数
        entity.setOrderSn(RechargeConfigMsg.rechargeGoldPrefix+ StrUtil.getOrderIdByUUId());//订单号
        entity.setRechargeId("");//平台交易号
        entity.setPayType(payType);//支付类型
        entity.setProductId(productId);//设备ID
        entity.setPrice(goldInfoEntity.getPrice());//价格
        entity.setStatus(RechargeUtil.isDirectPayType(payType)?PayStatusEnum.ALREADY_PAY.getCode():
                PayStatusEnum.NO_PAY.getCode());//状态
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//结束时间
        return entity;
    }
}
