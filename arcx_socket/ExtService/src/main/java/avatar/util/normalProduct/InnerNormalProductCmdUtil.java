package avatar.util.normalProduct;

import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.global.lockMsg.LockMsg;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.*;
import avatar.util.system.JsonUtil;

/**
 * 普通设备内部协议处理工具类
 */
public class InnerNormalProductCmdUtil {
    /**
     * 处理协议信息
     */
    public static void dealCmdMsg(InnerProductJsonMapMsg jsonMapMsg) {
        int cmd = jsonMapMsg.getCmd();
        switch (cmd) {
            case WebsocketInnerCmd.S2C_HEART :
                //心跳
                break;
            case WebsocketInnerCmd.S2C_START_GAME:
                //开始游戏
                startGame(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_PRODUCT_MSG:
                //设备信息
                productMsg(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_DOWN_CATCH:
                //下爪
                downCatch(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_GET_COIN:
                //获得币
                getCoin(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_COIN_PILE_TOWER:
                //堆塔
                pileTower(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_AUTO_SHOOT:
                //S2C自动发炮
                autoShoot(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_CANCEL_AUTO_SHOOT:
                //S2C取消自动发炮
                cancelAutoShoot(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_SETTLEMENT:
                //只结算
                settlement(jsonMapMsg);
                break;
            case WebsocketInnerCmd.S2C_SETTLEMENT_REFRESH:
                //S2C结算刷新
                settlementRefresh(jsonMapMsg);
                break;
        }
    }

    /**
     * 开始游戏
     */
    private static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        try {
            int productId = jsonMapMsg.getProductId();//设备ID
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                    2000);
            try {
                if (lock.lock()) {
                    int productType = ProductUtil.loadProductType(productId);//设备类型
                    if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
                        //娃娃机
                        DollInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }else if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
                        //推币机
                        CoinPusherInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }else if(productType== ProductTypeEnum.PRESENT_MACHINE.getCode()){
                        //礼品机
                        PresentInnerReceiveDealUtil.startGame(jsonMapMsg);
                    }
                }
            }catch (Exception e){
                ErrorDealUtil.printError(e);
            }finally {
                lock.unlock();
            }
        }catch (Exception e){
            LogUtil.getLogger().info("处理普通设备返回开始游戏信息的时候，报异常，异常信息{}--------",
                    JsonUtil.mapToJson(jsonMapMsg.getDataMap()));
        }
    }

    /**
     * 设备信息
     */
    private static void productMsg(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                //更新设备信息
                ProductGamingUtil.updateGamingUserMsg(productId, jsonMapMsg.getResponseGeneralMsg());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 下爪
     */
    private static void downCatch(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备类型
                if (productType == ProductTypeEnum.DOLL_MACHINE.getCode()) {
                    //娃娃机
                    DollInnerReceiveDealUtil.downCatch(jsonMapMsg);
                }else if (productType == ProductTypeEnum.PRESENT_MACHINE.getCode()) {
                    //礼品机
                    PresentInnerReceiveDealUtil.downCatch(jsonMapMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获得币的处理
     */
    public static void getCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备类型
                int secondLevelType = ProductUtil.loadSecondType(productId);//设备二级分类
                if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
                    //处理推币机获得币
                    boolean flag = ProductUtil.checkPushCoinProductGetCoin(productId, jsonMapMsg);
                    if(flag) {
                        //推币机
                        if (ProductUtil.isLotteryProduct(secondLevelType)) {
                            //获得彩票的推币机
                            CoinPusherInnerReceiveDealUtil.getLotteryCoin(jsonMapMsg);
                        } else {
                            //获得币的推币机
                            CoinPusherInnerReceiveDealUtil.getCoin(jsonMapMsg);
                        }
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 堆塔
     */
    private static void pileTower(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int secondType = ProductUtil.loadSecondType(productId);//设备类型
                if (secondType == ProductSecondTypeEnum.PILE_TOWER.getCode()) {
                    //炼金塔
                    CoinPusherInnerReceiveDealUtil.pileTower(jsonMapMsg);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 自动发炮
     */
    private static void autoShoot(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备分类
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 取消自动发炮
     */
    private static void cancelAutoShoot(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备分类
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 结算
     */
    private static void settlement(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备分类
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 结算刷新
     */
    private static void settlementRefresh(InnerProductJsonMapMsg jsonMapMsg) {
        int productId = jsonMapMsg.getProductId();//设备ID
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_ROOM_DEAL_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                int productType = ProductUtil.loadProductType(productId);//设备分类
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
