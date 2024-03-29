package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.user.UserNoticePushUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 推币机堆塔通知定时器
 */
public class CoinPileTowerSendMsgTask extends ScheduledTask {

    private int productId;//设备ID

    private int isStop;//是否停止

    public CoinPileTowerSendMsgTask(int productId, int isStop) {
        super("推币机堆塔通知定时器");
        this.productId = productId;
        this.isStop = isStop;
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
                if(roomMsg!=null && roomMsg.getGamingUserId()!=0){
                    int userId = roomMsg.getGamingUserId();//游戏玩家
                    if(userId>0) {
                        //刷新设备时间
                        ProductDealUtil.updateProductTime(productId, userId, roomMsg);
                        //推送socket通知
                        UserNoticePushUtil.pileTower(userId, productId, isStop);
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
