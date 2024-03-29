package avatar.util.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.catchDoll.DollDownCatchTask;
import avatar.task.product.presentMachine.PresentDownCatchTask;
import avatar.task.product.pushCoin.CoinPusherOffLineTask;
import avatar.util.LogUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * 内部设备操作下线工具类
 */
public class InnerOffLineUtil {
    /**
     * 下线定时器
     */
    public static void offLineTask(int userId, int productId) {
        //查询设备缓存信息
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        int offLineTime = ProductUtil.productOffLineTime(productId);//设备下机时间
        if(offLineTime>0) {
            int productType = ProductUtil.loadProductType(productId);
            if (productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                //推币机
                SchedulerSample.delayed(offLineTime * 1000,
                        new CoinPusherOffLineTask(userId, productId, roomMsg.getOnProductTime()));
            }
        }else{
            LogUtil.getLogger().info("添加玩家{}在设备{}下线的定时器的时候，查询不到设备下机时间--------", userId, productId);
        }
    }

    /**
     * 夹娃娃定时下爪
     */
    public static void dollDownCatch(int productId, int userId) {
        //查询设备缓存信息
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(ProductUtil.productOffLineTime(productId)* 1000,
                new DollDownCatchTask(userId, productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime()));
    }

    /**
     * 礼品机定时下爪
     */
    public static void presentDownCatch(int productId, int userId) {
        //查询设备缓存信息
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(ProductUtil.productOffLineTime(productId)* 1000,
                new PresentDownCatchTask(userId, productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime()));
    }
}
