package avatar.util.product;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.InnoReturnCoinTask;
import avatar.task.innoMsg.SyncInnoAutoPushCoinTask;
import avatar.util.LogUtil;
import avatar.util.basic.general.AwardUtil;
import avatar.util.basic.general.ImgUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static avatar.util.product.ProductUtil.isInnoProduct;

/**
 * 设备处理工具类
 */
public class ProductDealUtil {
    /**
     * 刷新设备时间
     */
    public static void updateProductTime(int productId, int userId, ProductRoomMsg msg) {
        LogUtil.getLogger().info("刷新设备{}-{}的时间信息，在线玩家{}-------", productId,
                ProductUtil.loadProductName(productId), userId);
        long nowTime = TimeUtil.getNowTime();
        msg.setPushCoinOnTime(TimeUtil.getNowTime());//投币时间
        //保存缓存
        ProductRoomDao.getInstance().setCache(productId, msg);
        //推送前端刷新
        productRefreshTime(userId, productId, nowTime);
    }

    /**
     * 设备刷新时间
     */
    public static void productRefreshTime(int userId, int productId, long time) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("devId", productId);//设备ID
        dataJson.put("rfTm", time);//刷新时间
        dataJson.put("lfTm", ProductUtil.loadProductLeftTime(time, productId));//剩余时间
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_REFRESH_TIME,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

    /**
     * 投币失败处理
     */
    public static void pushCoinFailDeal(int userId, int productId, InnoReceiveProductOperateMsg productOperateMsg) {
        //补回游戏币
        SchedulerSample.delayed(5, new InnoReturnCoinTask(productId, userId,
                productOperateMsg.getOnProductTime()));
    }

    /**
     * socket断下线处理
     */
    public static void socketOffDeal(int userId){
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if (list!=null && list.size()>0) {
            list.forEach(entity->{
                int productId = entity.getProductId();//设备ID
                //更新在线信息不在线
                UserOnlineUtil.onlineMsgNoOnline(userId, productId);
                //获取设备锁
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK + "_" + productId,
                        2000);
                try {
                    if (lock.lock()) {
                        //查询设备缓存信息
                        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        if (productRoomMsg != null && productRoomMsg.getGamingUserId() == userId) {
                            //推送自研取消自动投币
                            if(isInnoProduct(productId)){
                                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
                            }
                        }
                    }
                } catch (Exception e) {
                    ErrorDealUtil.printError(e);
                } finally {
                    lock.unlock();
                }
            });
        }
    }

    /**
     * 处理设备奖励
     */
    public static List<GeneralAwardMsg> dealMachineAward(int userId, int productId, int result, int getType,
            DollAwardCommodityMsg awardMsg) {
        List<GeneralAwardMsg> awardList = new ArrayList<>();
        if(result== CatchDollResultEnum.WIN.getCode()){
            //能量处理
            UserAttributeUtil.productAwardEnergyDeal(userId, productId, getType, awardList);
            //添加奖励信息
            if(awardMsg!=null){
                awardList.add(AwardUtil.initGeneralAwardMsg(awardMsg.getCommodityType(),
                        awardMsg.getAwardId(), ImgUtil.productAwardImg(awardMsg.getAwardImgId()), awardMsg.getAwardNum()));
            }
        }
        return awardList;
    }
}
