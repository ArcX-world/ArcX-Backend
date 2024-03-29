package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductPileTowerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 堆塔停止检测定时器
 */
public class PileStopCheckTask extends ScheduledTask {

    private int productId;//设备ID

    private int userId;//玩家ID

    private long onProductTime;//操作时间

    public PileStopCheckTask(int productId, int userId, long onProductTime) {
        super("堆塔停止检测定时器");
        this.productId = productId;
        this.userId = userId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //查询设备信息
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && roomMsg.getGamingUserId()==userId &&
                        roomMsg.getOnProductTime()==onProductTime){
                    PileTowerMsg msg = PileTowerMsgDao.getInstance().loadByProductId(productId);
                    long checkTime = TimeUtil.getNowTime()-msg.getPileTime();//检测时间
                    if(checkTime>=ProductConfigMsg.pileStopTime && msg.getTillTime()>=ProductConfigMsg.pileTillTime){
                        LogUtil.getLogger().info("炼金塔设备{}堆塔停止了，推送给玩家{}---------", productId, userId);
                        //初始化缓存
                        ProductPileTowerUtil.initMsg(msg);
                        //推送停止
                        SchedulerSample.delayed(100, new CoinPileTowerSendMsgTask(productId, YesOrNoEnum.YES.getCode()));
                    }else{
                        //继续检测
                        SchedulerSample.delayed(ProductConfigMsg.pileStopTime-checkTime,
                                new PileStopCheckTask(productId, userId, onProductTime));
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
