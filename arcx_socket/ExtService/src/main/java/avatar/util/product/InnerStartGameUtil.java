package avatar.util.product;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.global.Config;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.task.product.pushCoin.CoinPusherGetCoinTask;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;

/**
 * 设备内部操作开始游戏工具类
 */
public class InnerStartGameUtil {
    /**
     * 开始游戏初始化设备缓存信息
     */
    public static void startInitMsg(int productId, int userId, ResponseGeneralMsg responseGeneralMsg) {
        initStartGameProductMsg(productId, userId);
        //更新玩家在线信息
        UserOnlineUtil.startGameProduct(userId, productId, Config.getInstance().getLocalAddr(),
                Config.getInstance().getWebSocketPort());
        //设置游戏中玩家信息
        ProductGamingUtil.updateGamingUserMsg(productId, responseGeneralMsg);
        //刷新房间信息
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
    }

    /**
     * 重置开始游戏设备缓存
     */
    private static void initStartGameProductMsg(int productId, int userId) {
        //房间缓存
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        productRoomMsg.setOnProductTime(TimeUtil.getNowTime());//游戏时间
        productRoomMsg.setGamingUserId(userId);//游戏玩家ID
        productRoomMsg.setPushCoinOnTime(TimeUtil.getNowTime());//投币时间
        ProductRoomDao.getInstance().setCache(productId, productRoomMsg);
        if(ProductUtil.isDollMachine(productId)){
            //娃娃机
            initDollStartGame(productId);
        }
    }

    /**
     * 娃娃机缓存
     */
    private static void initDollStartGame(int productId) {
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        gamingMsg.setTime(gamingMsg.getTime()+1);
        gamingMsg.setInitalization(false);//是否设备初始化
        gamingMsg.setCatch(false);//是否已经操作
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**
     * 推币机获得币定时器
     */
    public static void getCoinTask(int productId, int userId) {
        //查询设备缓存信息
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        SchedulerSample.delayed(2000, new CoinPusherGetCoinTask(userId,
                productId, roomMsg.getOnProductTime()));
    }
}
