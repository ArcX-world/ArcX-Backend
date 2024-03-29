package avatar.util.product;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.code.basicConfig.CoinPusherConfigMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductSecondTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.gaming.PileTowerMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.task.product.pushCoin.CoinPileTowerSendMsgTask;
import avatar.task.product.pushCoin.PileStopCheckTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SyncInnoUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserAttributeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserNoticePushUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * 推币机设备内部接收到返回信息处理工具类
 */
public class CoinPusherInnerReceiveDealUtil {
    /**
     * 处理推币机返回
     */
    public static void sendCoinPusherRet(int userId, int operateState, int status, int getCoin, int productId){
        JSONObject dataJson = new JSONObject();//内容参数
        dataJson.put("flAmt", getCoin);//获得的币值
        dataJson.put("hdlTp", operateState);//操作类型
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));//玩家余额
        dataJson.put("devId", productId);//设备ID
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId,
                dataJson);
    }

    /**
     * 处理获得币返回信息
     */
    public static void dealGetCoinResultProductMsg(int userId, int result, int productId) {
        if(UserOnlineUtil.isOnline(userId)) {
            //推送客户端
            sendCoinPusherRet(userId, ProductOperationEnum.GET_COIN.getCode(), ClientCode.SUCCESS.getCode(), result, productId);
        }
    }

    /**
     * 投币
     */
    public static int pushCoin(int userId, int productId, boolean innoFreeLink) {
        int status = ClientCode.SUCCESS.getCode();//成功
        //查询设备价格，如果是发动技能中，则不扣除币值
        int cost = ProductUtil.productCost(productId);
        boolean flag = innoFreeLink || cost==0;//免费环节
        if(!flag){
            //普通游戏
            flag = UserBalanceUtil.costUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode(), cost);
        }
        if(flag) {
            int preCoinWeight = InnoProductUtil.userCoinWeight(userId, productId);//处理之前的权重等级
            if(!innoFreeLink) {
                //添加扣除的币值缓存
                ProductGamingUtil.addCostCoin(productId, cost);
            }
            //刷新设备缓存
            pushCoinRoomMsgDeal(productId);
            //处理权重
            SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
        }else{
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        }
        return status;
    }

    /**
     * 投币刷新缓存
     */
    private static void pushCoinRoomMsgDeal(int productId) {
        //查询设备房间信息
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        roomMsg.setPushCoinOnTime(TimeUtil.getNowTime());//上机时间
        ProductRoomDao.getInstance().setCache(productId, roomMsg);
    }

    /**
     * 退出设备
     */
    public static void offlineProduct(int productId, int userId) {
        //退出设备
        ProductOperateUtil.offLineProduct(userId, productId);
        //推送客户端
        sendCoinPusherRet(userId, ProductOperationEnum.OFF_LINE.getCode(), ClientCode.SUCCESS.getCode(), 0, productId);
        if(!ProductUtil.isInnoProduct(productId)) {
            //推送设备服务器下机通知
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**
     * 获得币后续处理
     */
    public static void getCoinDeal(int userId, int productId, int result){
        //查询设备价格
        int cost = ProductUtil.productCost(productId);
        if (cost > 0) {
            int preCoinWeight = ProductUtil.isInnoProduct(productId)?
                    InnoProductUtil.userCoinWeight(userId, productId):0;//自研设备权重等级
            int score = result*cost;//获得币需要乘上币值
            //添加玩家余额，在线分数信息
            ProductGamingUtil.addEarnCoin(userId, productId, score);
            //获得币缓存处理
            pushCoinRoomMsgDeal(productId);
            //处理返回信息
            dealGetCoinResultProductMsg(userId, score, productId);
            //处理自研设备NA值变化
            if(preCoinWeight>0){
                SyncInnoUtil.dealCoinWeight(preCoinWeight, userId, productId);
            }
        }
    }

    /**
     * 开始游戏
     */
    public static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        int status = InnerNormalProductUtil.loadClientCode(jsonMapMsg.getStatus());//返回状态
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        int secondType = 0;//设备二级分类
        if(ParamsUtil.isSuccess(status)){
            //查询设备价格
            int cost = ProductUtil.productCost(productId);
            boolean flag = UserBalanceUtil.costStartGame(productId, userId, cost);
            secondType = ProductUtil.loadSecondType(productId);//二级分类
            if(flag){
                //初始化币值缓存
                ProductGamingUtil.initProductCostCoinMsg(productId);
                if(secondType== ProductSecondTypeEnum.PILE_TOWER.getCode()){
                    //炼金塔（初始化炼金塔缓存）
                    PileTowerMsgDao.getInstance().setCache(productId, ProductGamingUtil.initPileTowerMsg(productId));
                }
                //添加扣除的币值缓存
                ProductGamingUtil.addCostCoin(productId, cost);
                //开始游戏初始化信息
                InnerStartGameUtil.startInitMsg(productId, userId,
                        InnoParamsUtil.initResponseGeneralMsg(ProductUtil.loadProductAlias(productId), userId));
                //添加获得币定时器
                InnerStartGameUtil.getCoinTask(productId, userId);
                //添加下机定时器
                InnerOffLineUtil.offLineTask(userId, productId);
                //推送上机投币操作
                pushOnlineOperate(userId, productId);
                //设备投币处理
                SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId, cost));
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        //推送客户端
        sendCoinPusherRet(userId, ProductOperationEnum.START_GAME.getCode(), status, 0, productId);
        if(ParamsUtil.isSuccess(status)){
            //添加操作日志
            UserOperateLogUtil.startGame(userId, productId);
            //推送彩票进度
            UserNoticePushUtil.pushLotteryNotice(productId, secondType, userId, 0, 0);
        }else{
            //推送普通设备服务器退出设备通知
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**
     * 推送上机设备操作
     */
    private static void pushOnlineOperate(int userId, int productId) {
        //投币
        InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_OPERATE,
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnerNormalProductWebsocketUtil.initProductOperateMap(productId, userId,
                        StrUtil.strToStrList(CoinPusherConfigMsg.pushCoinOperate)));
    }

    /**
     * 获得彩票并兑换成游戏币
     */
    public static void getLotteryCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        int result = jsonMapMsg.getDataMap().get("retNum")==null?0:
                (int) jsonMapMsg.getDataMap().get("retNum");//获得币结果
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();//设备信息通用参数
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();//玩家上机时间
        if(userId>0 && productId>0){
            //获取缓存信息
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                //添加彩票处理
                ProductGamingUtil.addLotteryResult(userId, result, productId);
                //获得币缓存处理
                ProductGamingUtil.flushPushCoinTime(productId);
                //处理返回信息
                dealGetCoinResultProductMsg(userId, result, productId);
            }
        }else{
            LogUtil.getLogger().info("接收到炼金塔获得币返回信息的时候返回的玩家ID{}或者设备ID{}为0-------", userId, productId);
        }
    }

    /**
     * 获得币的处理
     */
    public static void getCoin(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        int result = jsonMapMsg.getDataMap().get("retNum")==null?0:
                (int) jsonMapMsg.getDataMap().get("retNum");//获得币结果
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();//设备信息通用参数
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();//玩家上机时间
        if(userId>0 && productId>0){
            //获取缓存信息
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId && roomMsg.getOnProductTime()==onProductTime){
                if(result>0){
                    //获得币后续处理
                    getCoinDeal(userId, productId, result);
                }
            }
        }else{
            LogUtil.getLogger().info("接收到推币机获得币返回信息的时候返回的玩家ID{}或者设备ID{}为0-------", userId, productId);
        }
    }

    /**
     * 炼金塔堆塔
     */
    public static void pileTower(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();//设备通用信息参数
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();//玩家上机时间
        if(userId>0 && productId>0){
            //获取缓存信息
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(roomMsg.getGamingUserId()==userId &&
                    roomMsg.getOnProductTime()==onProductTime){
                //堆塔
                LogUtil.getLogger().info("玩家{}在推币机设备{}上堆塔--------", userId, productId);
                //更新堆塔时间
                updatePileTime(productId, userId, onProductTime);
                //处理堆塔奖励
                ProductPileTowerUtil.dealPileTowerAward(userId, productId);
            }
        }else{
            LogUtil.getLogger().info("接收到推币机堆塔返回信息的时候返回的玩家ID{}或者设备ID{}为0-------", userId, productId);
        }
    }

    /**
     * 更新堆塔时间
     */
    private static void updatePileTime(int productId, int userId, long onProductTime) {
        PileTowerMsg msg = PileTowerMsgDao.getInstance().loadByProductId(productId);
        if(msg!=null){
            long oriTime = msg.getPileTime();//初始时间
            msg.setPileTime(TimeUtil.getNowTime());//堆塔时间
            msg.setTillTime(msg.getTillTime()+1);//堆塔持续次数
            PileTowerMsgDao.getInstance().setCache(productId, msg);
            //推送堆塔通知
            if(msg.getTillTime()>=ProductConfigMsg.pileTillTime){
                SchedulerSample.delayed(100, new CoinPileTowerSendMsgTask(productId, YesOrNoEnum.NO.getCode()));
            }
            //添加定时器
            if(oriTime==0){
                LogUtil.getLogger().info("炼金塔设备{}开始添加堆塔定时器了--------", productId);
                //定时检测堆塔时间信息
                SchedulerSample.delayed(ProductConfigMsg.pileStopTime,
                        new PileStopCheckTask(productId, userId, onProductTime));
            }
        }
    }
}
