package avatar.util.log;

import avatar.entity.log.UserOperateLogEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.log.UserOperateTypeEnum;
import avatar.module.log.UserOperateLogDao;
import avatar.util.product.ProductUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserPropertyUtil;
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
    public static void costBalance(long num, int userId, int productId, int commodityType, String msg) {
        if (userId != 0 && num != 0) {
            int userOperateType = 0;
            if (commodityType == CommodityTypeEnum.GOLD_COIN.getCode()) {
                //金币
                userOperateType = UserOperateTypeEnum.COST_GOLG_COIN.getCode();
            } else if (commodityType == CommodityTypeEnum.AXC.getCode()) {
                //axc
                userOperateType = UserOperateTypeEnum.COST_AXC.getCode();
            }
            String log = "【" + UserOperateTypeEnum.getNameByCode(userOperateType) + "】";//日志
            //消费数量
            if (num > 0) {
                log += "【+" + StrUtil.toMoneySize(num) + "】";
            } else {
                log += "【" + StrUtil.toMoneySize(num) + "】";
            }
            if(!StrUtil.checkEmpty(msg)){
                //签到、等级特权、邀请码、充值
                log += "【" + msg + "】";
            } else if (productId > 0) {
                //设备上玩耍
                log += "【" + ProductUtil.productLog(productId) + "】";
            }
            //查询金币余额
            long balance = UserBalanceUtil.getUserBalance(userId, commodityType);
            log += "【" + CommodityTypeEnum.getNameByCode(commodityType) + StrUtil.toMoneySize(balance) + "】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 结算游戏
     * 格式：【结算游戏】【1-娃娃机-青蛙】【金币100】
     */
    public static void settlement(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            //查询设备信息
            String log = "【" + UserOperateTypeEnum.SETTLEMENT_GAME.getName() + "】";//日志
            log += "【" + ProductUtil.productLog(productId) + "】";//设备信息
            log += "【" + CommodityTypeEnum.GOLD_COIN.getName() + StrUtil.toMoneySize(UserBalanceUtil.getUserBalance(userId,
                    CommodityTypeEnum.GOLD_COIN.getCode())) + "】";
            //添加数据
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, UserOperateTypeEnum.SETTLEMENT_GAME.getCode(),
                    log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 开始游戏
     * 格式：【开始游戏】【1-娃娃机-青蛙】【余额100】
     */
    public static void startGame(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.START_GAME.getName() + "】";//日志
            log += "【" + ProductUtil.productLog(productId) + "】";//设备信息
            if (ProductUtil.isInnoProduct(productId)) {
                //自研设备加倍率
                log += "【倍率】" + StrUtil.toMoneySize(ProductUtil.productCost(productId));
            }
            long userBalance = UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()) + ProductUtil.productCost(productId);//玩家余额
            log += "【游戏币" + StrUtil.toMoneySize(userBalance) + "】";//需要补一次价格
            //添加数据
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.START_GAME.getCode(),
                            log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 进入设备
     * 格式：【进入设备】【1-娃娃机-青蛙】
     */
    public static void joinProduct(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.JOIN_PRODUCT.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            //添加数据
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.JOIN_PRODUCT.getCode(),
                            log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 退出设备
     * 格式：【退出设备】【1-娃娃机-青蛙】
     */
    public static void existProduct(int userId, int productId) {
        if (userId != 0 && productId != 0) {
            String log = "【" + UserOperateTypeEnum.EXIT_PRODUCT.getName() + "】";
            log += "【" + ProductUtil.productLog(productId) + "】";
            //添加数据
            UserOperateLogDao.getInstance().insert(
                    initUserOperateLogEntity(userId, UserOperateTypeEnum.EXIT_PRODUCT.getCode(),
                            log, UserUtil.loadUserIp(userId)));
        }
    }

    /**
     * 消费道具
     */
    public static void costProperty(long num, int userId, int propertyType, String msg){
        if(userId!=0 && num!=0){
            int userOperateType = UserOperateTypeEnum.COST_PROPERTY.getCode();
            //日志
            String log = "【"+ PropertyTypeEnum.getNameByCode(propertyType)+"】";
            //消费数量
            if(num>0){
                log += "【+"+ StrUtil.toMoneySize(num)+"】";
            }else{
                log += "【"+StrUtil.toMoneySize(num)+"】";
            }
            if(!StrUtil.checkEmpty(msg)){
                log += "【"+msg+"】";
            }
            //查询道具余额
            long balance = UserPropertyUtil.getUserProperty(userId, propertyType);
            log += "【"+PropertyTypeEnum.getNameByCode(propertyType)+StrUtil.toMoneySize(balance)+"】";
            UserOperateLogDao.getInstance().insert(initUserOperateLogEntity(userId, userOperateType,
                    log, UserUtil.loadUserIp(userId)));
        }
    }
}
