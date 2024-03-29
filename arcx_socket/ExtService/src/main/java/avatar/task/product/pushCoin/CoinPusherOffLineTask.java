package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推币机下机定时器
 */
public class CoinPusherOffLineTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    private long onProductTime;//上机时间

    public CoinPusherOffLineTask(int userId, int productId, long onProductTime) {
        super("推币机下机定时器");
        this.userId = userId;
        this.productId = productId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        boolean flag = true;//是否继续启用定时器
        LogUtil.getLogger().info("玩家{}在推币机设备{}上定时下机---------",userId, productId);
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //获取设备房间信息
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null){
                    flag = false;//成功处理下机
                    //是当前玩家并且是当前本次操作
                    if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                        int offLineTime = ProductUtil.productOffLineTime(productId);//设备下机时间
                        LogUtil.getLogger().info("推币机{}检测定时下机更新时间{}，消耗时间{}", productId, roomMsg.getPushCoinOnTime(),
                                (TimeUtil.getNowTime()-roomMsg.getPushCoinOnTime()));
                        if (((TimeUtil.getNowTime() - roomMsg.getPushCoinOnTime())>=offLineTime*1000)) {
                            //判断是否中奖锁定
                            if(ProductGamingUtil.isProductAwardLock(productId)){
                                LogUtil.getLogger().info("玩家{}在推币机设备{}上到时间下机，但是中奖锁定中，重新计时---------",
                                        userId, productId);
                                SchedulerSample.delayed((offLineTime - 1) * 1000,
                                        new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
                            }else {
                                LogUtil.getLogger().info("玩家{}在推币机设备{}上成功定时下机---------", userId, productId);
                                //设备操作
                                ProductSocketOperateService.coinPusherOperate(productId, ProductOperationEnum.OFF_LINE.getCode(), userId);
                            }
                        } else {
                            long checkTime = (offLineTime - 1) * 1000 - (TimeUtil.getNowTime() - roomMsg.getPushCoinOnTime());
                            if (checkTime < 1000) {
                                checkTime = 1000;
                            }
                            SchedulerSample.delayed(checkTime,
                                    new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
                        }
                    }else{
                        LogUtil.getLogger().info("推币机{}下线的定时器断了，原参数在线玩家{}设备ID{}上机时间{}---------",
                                productId,userId, productId, onProductTime);
                        LogUtil.getLogger().info("推币机{}下线的定时器断了，对比参数在线玩家{}设备ID{}上机时间{}---------",
                                productId,roomMsg.getGamingUserId(), productId, roomMsg.getOnProductTime());
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        //下机失败，重新处理，继续启用定时器
        if(flag){
            LogUtil.getLogger().info("玩家{}在推币机设备{}上下机失败，重新处理--------", userId, productId);
            SchedulerSample.delayed(100, new CoinPusherOffLineTask(userId,productId, onProductTime));
        }
    }
}
