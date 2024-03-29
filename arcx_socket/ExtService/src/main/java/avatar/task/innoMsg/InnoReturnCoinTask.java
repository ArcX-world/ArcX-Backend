package avatar.task.innoMsg;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.product.CoinPusherInnerReceiveDealUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserBalanceUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 自研设备返还游戏币定时器
 */
public class InnoReturnCoinTask extends ScheduledTask {

    private int productId;//设备ID

    private int userId;//玩家ID

    private long onProductTime;//上机时间

    public InnoReturnCoinTask(int productId, int userId, long onProductTime) {
        super("自研设备返还游戏币定时器");
        this.productId = productId;
        this.userId = userId;
        this.onProductTime = onProductTime;
    }

    @Override
    public void run() {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //查询设备缓存
                ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
                if(productRoomMsg.getGamingUserId()==userId && productRoomMsg.getOnProductTime()==onProductTime){
                    int cost = ProductUtil.productCost(productId);//游戏币数量
                    LogUtil.getLogger().error("自研设备{}投币故障，返还玩家{}游戏币数量{}------", productId, userId, cost);
                    //去除扣除的币值缓存
                    long sumCostCoin = ProductGamingUtil.costCostCoin(productId, cost);
                    if(sumCostCoin>0) {
                        int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);//添加之前的权重等级
                        //添加玩家余额
                        UserBalanceUtil.addUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
                        //推送返回币结果
                        CoinPusherInnerReceiveDealUtil.dealGetCoinResultProductMsg(userId, cost, productId);
                        //处理权重
                        SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
                    }
                }else{
                    LogUtil.getLogger().error("自研设备{}投币故障，设备信息不匹配，返还玩家{}游戏币失败------", productId, userId);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
