package avatar.task.product.presentMachine;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.service.product.ProductSocketOperateService;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 礼品机下爪定时器
 */
public class PresentOffLineTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    private int time;//次数

    private int gamingState;//游戏环节

    private long onProductTime;//上机时间

    public PresentOffLineTask(int userId, int productId, int time, int gamingState, long onProductTime) {
        super("礼品机定时下机");
        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.gamingState = gamingState;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        boolean flag = true;//是否继续启用定时器
        LogUtil.getLogger().info("玩家{}在设备{}上抓礼品定时下机---------",userId,productId);
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //设备信息
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null){
                    flag = false;//成功下机
                    //是当前玩家并且是当前本次操作
                    if(roomMsg.getGamingUserId()==userId && ProductUtil.startGameTime(productId) ==time
                            && roomMsg.getOnProductTime()==onProductTime){
                        LogUtil.getLogger().info("玩家{}在设备{}上抓礼品成功定时下机---------",userId,productId);
                        //设备操作
                        ProductSocketOperateService.presentOperate(productId, ProductOperationEnum.OFF_LINE.getCode(),userId);
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
            LogUtil.getLogger().info("玩家{}在礼品机设备{}上下机失败，重新处理--------", userId, productId);
            SchedulerSample.delayed(100, new
                    PresentOffLineTask(userId,productId,time,gamingState, onProductTime));
        }
    }
}
