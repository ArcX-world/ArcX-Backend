package avatar.task.product.presentMachine;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.PresentInnerReceiveDealUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.ArrayList;

/**
 * 礼品机下爪结果检测定时器
 */
public class PresentResultCheckTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    private int time;//次数

    private long onProductTime;//上机时间

    private long startGameTime;//缓存的开始游戏时间

    public PresentResultCheckTask(int userId, int productId, int time, long onProductTime, long startGameTime) {
        super("礼品机定时下爪结果检测");
        this.userId = userId;
        this.productId = productId;
        this.time = time;
        this.onProductTime = onProductTime;
        this.startGameTime = startGameTime;
    }

    @Override
    public void run() {
        //获取设备锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null && gamingMsg!=null){
                    //是当前玩家并且是当前本次操作
                    if(roomMsg.getGamingUserId()==userId && gamingMsg.getTime()==time
                            && roomMsg.getOnProductTime()==onProductTime){
                        LogUtil.getLogger().info("玩家{}在设备{}上抓礼品定时下爪结果检测的时候，还没接收到服务器返回---------",userId,productId);
                        //处理获取结果后的设备信息
                        PresentInnerReceiveDealUtil.dealResultProductMsg(CatchDollResultEnum.LOSE.getCode(),
                                userId, productId, time, onProductTime, new ArrayList<>());
                    }else if(gamingMsg.isInitalization()){
                        //检测初始化
                        PresentInnerReceiveDealUtil.checkInit(productId, startGameTime, userId);
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
