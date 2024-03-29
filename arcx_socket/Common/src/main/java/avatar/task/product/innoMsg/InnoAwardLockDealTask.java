package avatar.task.product.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.innoMsg.InnoAwardLockMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.crossServer.CrossServerMsgUtil;
import avatar.util.product.ProductGamingUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 中奖锁处理定时器
 */
public class InnoAwardLockDealTask extends ScheduledTask {

    //中奖锁信息
    private InnoAwardLockMsg innoAwardLockMsg;

    //设备ID
    private int productId;

    public InnoAwardLockDealTask(InnoAwardLockMsg innoAwardLockMsg, int productId) {
        super("中奖锁处理定时器");
        this.innoAwardLockMsg = innoAwardLockMsg;
        this.productId = productId;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg.getGamingUserId()==innoAwardLockMsg.getUserId() &&
                        CrossServerMsgUtil.isArcxServer(innoAwardLockMsg.getServerSideType())){
                    LogUtil.getLogger().info("更新设备{}更新中奖锁信息--------", productId);
                    //更新中奖锁信息
                    ProductGamingUtil.updateProductAwardLockMsg(productId, innoAwardLockMsg.getIsStart());
                }else{
                    LogUtil.getLogger().error("设备{}中奖锁处理定时器关闭，接收到的信息不是当前上机玩家---------", productId);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
