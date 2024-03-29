package avatar.util.log;


import avatar.entity.log.UserOperateLogEntity;
import avatar.global.enumMsg.basic.CommodityTypeEnum;
import avatar.global.enumMsg.log.UserOperateTypeEnum;
import avatar.module.log.UserOperateLogDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.UserUtil;

/**
 * 玩家操作日志
 */
public class UserOperateLogUtil {
    /**
     * 填充玩家操作日志实体信息
     */
    private static UserOperateLogEntity initUserOperateLogEntity(int userId, int operateType, String log, String ip) {
        UserOperateLogEntity entity = new UserOperateLogEntity();
        entity.setUserId(userId);//玩家ID
        entity.setOperateType(operateType);//操作类型
        entity.setOperateLog(log);//操作日志
        entity.setOperateIp(ip);//操作IP
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 消费余额
     * 格式：【消费金币】【+100】【签到】【金币100】
     * 格式：【消费金币】【+100】【微信支付】【金币100】
     * 格式：【消费金币】【-100】【1-娃娃机-青蛙】【金币100】
     */
    public static void costBalance(long num, int userId, int commodityType, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = 0;
            if(commodityType==CommodityTypeEnum.AXC.getCode()){
                //axc
                userOperateType = UserOperateTypeEnum.COST_AXC.getCode();
            }
            String log = "【"+UserOperateTypeEnum.getNameByCode(userOperateType)+"】";//日志
            //消费数量
            if(num>0){
                log += "【+"+ StrUtil.toMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                //签到、等级特权、邀请码、充值
                log += "【"+msg+"】";
            }
            //查询金币余额
            long balance = UserBalanceUtil.getUserBalance(userId, commodityType);
            log += "【"+CommodityTypeEnum.getNameByCode(commodityType)+StrUtil.toMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 消费USDT
     */
    public static void costUsdt(double num, int userId, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = UserOperateTypeEnum.COST_USDT.getCode();
            String log = "";
            //消费数量
            if(num>0){
                log += "【+"+ StrUtil.toFourMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toFourMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                log += "【"+msg+"】";
            }
            //查询道具余额
            double balance = UserUsdtUtil.usdtBalance(userId);
            log += "【"+StrUtil.toFourMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

}
