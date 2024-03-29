package avatar.service.normal;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.data.product.general.ResponseGeneralMsg;
import avatar.data.product.normalProduct.RequestGeneralMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.module.product.info.ProductAliasDao;
import avatar.service.jedis.RedisLock;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.ProductGamingUtil;
import avatar.util.product.ProductUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * 普通内部设备信息处理实现类
 */
public class InnerProductService {
    /**
     * 订阅的设备信息
     */
    public static void describeProductMsg(ResponseGeneralMsg responseGeneralMsg) {
        String alias = responseGeneralMsg.getAlias();//设备号
        int productId = ProductAliasDao.getInstance().loadByAlias(alias);
        if(productId>0){
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    //更新设备信息
                    ProductGamingUtil.updateGamingUserMsg(productId, responseGeneralMsg);
                    //刷新房间信息
                    SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 订阅的获得币信息
     */
    public static void describeGetCoinMsg(ResponseGeneralMsg responseGeneralMsg, int result) {
        int productId = ProductAliasDao.getInstance().loadByAlias(responseGeneralMsg.getAlias());
        if(productId>0) {
            int cost = ProductUtil.productCost(productId);//设备花费
            if(cost>0) {
                int productType = ProductUtil.loadProductType(productId);//设备类型
                int secondLevelType = ProductUtil.loadSecondType(productId);//设备二级分类
                if (productType == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()) {
                    //推币机
                    if (ProductUtil.isLotteryProduct(secondLevelType)) {
                        //获得彩票的推币机
                        getLotteryCoin(responseGeneralMsg, secondLevelType, result, cost);
                    } else {
                        //获得币的推币机
                        getCoin(responseGeneralMsg, result, cost);
                    }
                }
            }
        }
    }

    /**
     * 推币机获得币
     */
    private static void getCoin(ResponseGeneralMsg responseGeneralMsg, int result, int cost) {
        int userId = responseGeneralMsg.getUserId();//玩家ID
        int serverSideType = responseGeneralMsg.getServerSideType();//服务端类型
    }

    /**
     * 彩票机类型推币机获得币
     */
    private static void getLotteryCoin(ResponseGeneralMsg responseGeneralMsg, int secondLevelType,
            int result, int cost) {
        int userId = responseGeneralMsg.getUserId();//玩家ID
        int serverSideType = responseGeneralMsg.getServerSideType();//服务端类型
    }

    /**
     * 设备占用检测
     */
    public static void productOccupyCheck(ResponseGeneralMsg responseGeneralMsg) {
        //查询设备ID
        int productId = ProductAliasDao.getInstance().loadByAlias(responseGeneralMsg.getAlias());
        if(productId>0) {
            int gamingUserId = 0;
            //查询游戏中玩家信息
            ProductGamingUserMsg gamingUserMsg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
            if(gamingUserMsg.getServerSideType()==responseGeneralMsg.getUserId() &&
                    gamingUserMsg.getServerSideType()==responseGeneralMsg.getServerSideType()){
                //游戏中
                gamingUserId = gamingUserMsg.getUserId();
            }
            //请求信息
            RequestGeneralMsg requestGeneralMsg = InnerNormalProductWebsocketUtil.
                    initRequestGeneralMsg(gamingUserId, productId);
            //发送设备中心信息
            InnerNormalProductWebsocketUtil.sendMsg(WebsocketInnerCmd.C2S_PRODUCT_OCCUPY_CHECK,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    requestGeneralMsg);
        }
    }
}
