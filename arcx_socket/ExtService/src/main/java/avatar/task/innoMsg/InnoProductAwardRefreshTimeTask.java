package avatar.task.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.lockMsg.LockMsg;
import avatar.module.innoMsg.SelfProductAwardMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.ProductDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;
import com.yaowan.game.common.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备中奖刷新时间定时器
 */
public class InnoProductAwardRefreshTimeTask extends ScheduledTask {
    private int productId;//设备ID

    private int userId;//玩家ID

    public InnoProductAwardRefreshTimeTask(int productId, int userId) {
        super("自研设备中奖刷新时间定时器");
        this.productId = productId;
        this.userId = userId;
    }

    @Override
    public void run() {
        boolean reflushFlag = true;//是否刷新下一次
        RedisLock productRoomLock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (productRoomLock.lock()) {
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SELF_PRODUCT_AWARD_LOCK + "_" + productId,
                        2000);
                try {
                    if (lock.lock()) {
                        //更换的列表
                        List<Long> awardList = new ArrayList<>();
                        //查询中奖列表
                        List<Long> list = SelfProductAwardMsgDao.getInstance().loadByProductId(productId);
                        boolean flushTimeFlag = true;//是否刷新时间的标志
                        //查询设备信息
                        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                        long onProductTime = roomMsg.getOnProductTime();//上机时间
                        if(roomMsg.getGamingUserId()!=userId){
                            flushTimeFlag = false;//是否刷新下一次：否
                            reflushFlag = false;//是否刷新下一次：否
                        }else{
                            ProductUtil.dealSelfProductAwardMsg(list, awardList, onProductTime);
                        }
                        //重置中奖缓存
                        SelfProductAwardMsgDao.getInstance().setCache(productId, awardList);
                        if(awardList.size()==0){
                            flushTimeFlag = false;//是否刷新下一次
                            reflushFlag = false;//是否刷新下一次
                        }
                        if(flushTimeFlag){
                            LogUtil.getLogger().info("处理自研设备{}中奖刷新时间--------", productId);
                            //刷新设备时间
                            ProductDealUtil.updateProductTime(productId, userId, roomMsg);
                        }
                    }
                } catch (Exception e) {
                    ErrorDealUtil.printError(e);
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            ErrorDealUtil.printError(e);
        } finally {
            productRoomLock.unlock();
        }
        //走下一次刷新时间的检测
        if(reflushFlag){
            SchedulerSample.delayed(ProductConfigMsg.innoProductAwardRefreshTime, new InnoProductAwardRefreshTimeTask(productId, userId));
        }
    }
}
