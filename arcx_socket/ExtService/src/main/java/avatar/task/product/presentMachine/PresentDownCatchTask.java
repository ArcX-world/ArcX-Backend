package avatar.task.product.presentMachine;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 礼品机下爪定时器
 */
public class PresentDownCatchTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    private int time;//次数

    private long onProductTime;//上机时间

    public PresentDownCatchTask(int userId, int productId, int time, long onProductTime) {
        super("礼品机定时下爪");
        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        LogUtil.getLogger().info("玩家{}在设备{}上礼品机定时下爪---------",userId,productId);
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && gamingMsg!=null){
                    //是当前玩家并且是当前本次操作
                    if(roomMsg.getGamingUserId()==userId && gamingMsg.getTime()==time && !gamingMsg.isCatch()
                            && roomMsg.getOnProductTime()==onProductTime){
                        LogUtil.getLogger().info("玩家{}在设备{}上礼品机成功定时操作下爪---------",userId,productId);
                        //设备操作
                        ProductSocketOperateService.catchDollOperate(productId, ProductOperationEnum.CATCH.getCode(),userId);
                    }else{
                        LogUtil.getLogger().info("娃娃机下爪定时器失败，次数对比{}:{}，是否下爪中{}，设备时间{}:{}-------",
                                gamingMsg.getTime(), time, gamingMsg.isCatch(), roomMsg.getOnProductTime(), onProductTime);
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
