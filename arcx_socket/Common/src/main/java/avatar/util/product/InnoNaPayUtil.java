package avatar.util.product;

import avatar.data.product.innoNaPay.InnoNaPayUserStatusMsg;
import avatar.entity.product.innoNaPay.InnoNaPayMoneyConfigEntity;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.module.product.innoNaPay.InnoNaPayMoneyConfigDao;
import avatar.module.product.innoNaPay.InnoNaPayUserStatusDao;
import avatar.module.recharge.commodity.RechargeGoldOrderDao;
import avatar.util.system.TimeUtil;

/**
 * 自研付费NA工具类
 */
public class InnoNaPayUtil {
    /**
     * 玩家是否已经支付
     */
    public static boolean isPay(int userId){
        InnoNaPayUserStatusMsg msg = InnoNaPayUserStatusDao.getInstance().loadMsg(userId);
        return msg.isPayFlag();
    }

    /**
     * 填充自研NA玩家支付状态信息
     */
    public static InnoNaPayUserStatusMsg initInnoNaPayUserStatusMsg(int userId) {
        InnoNaPayUserStatusMsg msg = new InnoNaPayUserStatusMsg();
        msg.setUserId(userId);//玩家ID
        msg.setPayFlag(loadPayFlag(userId));//是否支付
        msg.setRefreshTime(TimeUtil.getNowTime());//刷新时间
        return msg;
    }

    /**
     * 获取是否已经支付
     */
    private static boolean loadPayFlag(int userId) {
        boolean flag;
        //查询NA金额配置信息
        InnoNaPayMoneyConfigEntity entity = InnoNaPayMoneyConfigDao.getInstance().loadMsg();
        int timeRange = entity.getTimeRange();//时间范围（天）
        double money = entity.getMoney();//累计金额
        double sumMoney = RechargeGoldOrderDao.getInstance().loadDbUserAmount(userId, timeRange);//统计金额（游戏币充值）
        if(money>0){
            //根据累计金额统计
            flag = sumMoney>money;
        }else{
            //有充值过就行
            flag = sumMoney>0;
        }
        return flag;
    }

    /**
     * 处理返回信息
     */
    public static InnoNaPayUserStatusMsg dealInnoNaPayUserStatusMsg(InnoNaPayUserStatusMsg msg) {
        if((TimeUtil.getNowTime()-msg.getRefreshTime())>=ProductConfigMsg.inoNaPayCheckTime) {
            int userId = msg.getUserId();//玩家ID
            msg.setPayFlag(loadPayFlag(userId));//是否支付
            msg.setRefreshTime(TimeUtil.getNowTime());//刷新时间
            InnoNaPayUserStatusDao.getInstance().setCache(userId, msg);
        }
        return msg;
    }
}
