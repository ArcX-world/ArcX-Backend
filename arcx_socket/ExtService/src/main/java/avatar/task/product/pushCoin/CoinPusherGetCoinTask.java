package avatar.task.product.pushCoin;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
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
 * 推币机获得币定时器
 */
public class CoinPusherGetCoinTask extends ScheduledTask {
    private int userId;//玩家ID

    private int productId;//设备ID

    private long onProductTime;//上机时间

    public CoinPusherGetCoinTask(int userId, int productId, long onProductTime) {
        super("推币机获得币定时器");
        this.userId = userId;
        this.productId = productId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        boolean flag = true;//是否继续启用定时器
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //查询设备房间信息
                ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(roomMsg!=null){
                    String productName = ProductUtil.loadSecondTypeName(ProductUtil.loadSecondType(productId));//获取设备二级分类名称
                    if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                        //设备操作
                        ProductSocketOperateService.coinPusherOperate(productId, ProductOperationEnum.GET_COIN.getCode(),userId);
                    }else{
                        LogUtil.getLogger().info("{}获得币的定时器断了，原参数在线玩家{}设备ID{}上机时间{}---------",
                                productName, userId, productId, onProductTime);
                        LogUtil.getLogger().info("{}获得币的定时器断了，对比参数在线玩家{}设备ID{}上机时间{}---------",
                                productName, roomMsg.getGamingUserId(), productId,
                                roomMsg.getOnProductTime());
                        flag = false;
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        if(flag){
            //继续推送获得币
            SchedulerSample.delayed(ProductConfigMsg.getCoinTime,
                    new CoinPusherGetCoinTask(userId,productId,onProductTime));
        }
    }
}
