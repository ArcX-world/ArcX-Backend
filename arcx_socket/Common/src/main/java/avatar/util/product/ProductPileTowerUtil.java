package avatar.util.product;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.entity.product.pileTower.ProductPileTowerConfigEntity;
import avatar.entity.product.pileTower.ProductPileTowerUserMsgEntity;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.pileTower.ProductPileTowerConfigDao;
import avatar.module.product.pileTower.ProductPileTowerUserMsgDao;
import avatar.util.LogUtil;
import avatar.util.log.CostUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserNoticePushUtil;

/**
 * 设备炼金塔堆塔工具类
 */
public class ProductPileTowerUtil {
    /**
     * 处理堆塔奖励
     */
    public static void dealPileTowerAward(int userId, int productId) {
        //查询炼金塔堆塔配置信息
        ProductPileTowerConfigEntity configEntity = ProductPileTowerConfigDao.getInstance().loadMsg();
        if(configEntity!=null) {
            //查询玩家堆塔信息
            ProductPileTowerUserMsgEntity userMsgEntity = ProductPileTowerUserMsgDao.getInstance().loadByUserId(userId);
            if(userMsgEntity==null || (TimeUtil.getNowTime()- TimeUtil.strToLong(userMsgEntity.getCreateTime()))
                    >configEntity.getIntervalTime()*1000){
                //没有堆塔信息或者超过缓存时间，添加并推送信息
                int minNum = configEntity.getMinNum();//最小值
                int maxNum = configEntity.getMaxNum();//最大值
                if(minNum>0 && maxNum>0){
                    int awardNum = StrUtil.loadInterValNum(minNum, maxNum);//随机数
                    //添加玩家奖励
                    CostUtil.addProductPileTowerAward(userId, productId, awardNum);
                    //推送socket通知
                    UserNoticePushUtil.pushPileTowerAward(userId, productId, awardNum, configEntity.getAwardImgId());
                }else{
                    LogUtil.getLogger().info("处理玩家{}在设备{}上堆塔奖励的时候，赠送币值配置有问题-------", userId, productId);
                }
            }
        }else{
            LogUtil.getLogger().info("处理玩家{}在设备{}上堆塔奖励的时候，查询不到炼金塔堆塔配置信息----------", userId, productId);
        }
    }

    /**
     * 填充设备炼金塔堆塔玩家信息
     */
    public static ProductPileTowerUserMsgEntity initProductPileTowerUserMsgEntity(int userId, int productId, int num){
        ProductPileTowerUserMsgEntity entity = new ProductPileTowerUserMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setNum(num);//游戏币数
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 重置堆塔信息
     */
    public static void initMsg(PileTowerMsg msg){
        msg.setPileTime(0);//时间
        msg.setTillTime(0);//堆塔连续次数
        PileTowerMsgDao.getInstance().setCache(msg.getProductId(), msg);
    }

}
