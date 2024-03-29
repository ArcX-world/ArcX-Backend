package avatar.util.product;

import avatar.data.basic.award.GeneralAwardMsg;
import avatar.data.product.gamingMsg.DollAwardCommodityMsg;
import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.data.product.normalProduct.InnerProductJsonMapMsg;
import avatar.data.product.normalProduct.ProductGeneralParamsMsg;
import avatar.global.code.basicConfig.PresentConfigMsg;
import avatar.global.code.basicConfig.ProductConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.award.EnergyExchangeGetTypeEnum;
import avatar.global.enumMsg.product.info.CatchDollResultEnum;
import avatar.global.enumMsg.product.info.GamingStateEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.gaming.DollGamingMsgDao;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.gaming.ProductStartTimeDao;
import avatar.service.product.ProductSocketOperateService;
import avatar.task.product.general.AddPresentAwardTask;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.task.product.presentMachine.PresentOffLineTask;
import avatar.task.product.presentMachine.PresentResultCheckTask;
import avatar.util.LogUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼品机设备内部接收到返回信息处理工具类
 */
public class PresentInnerReceiveDealUtil {
    /**
     * 开始游戏逻辑处理
     */
    public static void startGame(InnerProductJsonMapMsg jsonMapMsg) {
        int status = InnerNormalProductUtil.loadClientCode(jsonMapMsg.getStatus());//返回状态
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        int time = 0;//游戏次数
        if(ParamsUtil.isSuccess(status)){
            //查询设备价格
            int cost = ProductUtil.productCost(productId);
            boolean flag = UserBalanceUtil.costStartGame(productId, userId, cost);
            if(flag){
                time = ProductUtil.startGameTime(productId);//游戏次数
                //初始化币值缓存
                ProductGamingUtil.initProductCostCoinMsg(productId);
                //添加扣除的币值缓存
                ProductGamingUtil.addCostCoin(productId, cost);
                //开始游戏初始化信息
                InnerStartGameUtil.startInitMsg(productId, userId, jsonMapMsg.getResponseGeneralMsg());
                //设置上机时间
                ProductStartTimeDao.getInstance().setCache(productId, TimeUtil.getNowTime());
                //添加下爪定时器
                InnerOffLineUtil.presentDownCatch(productId, userId);
                //设备投币处理
                SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId, cost));
            }else{
                status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
            }
        }
        //推送客户端
        sendPresentRet(userId, ProductOperationEnum.START_GAME.getCode(), status, 0, productId, null);
        if(ParamsUtil.isSuccess(status) && time==0){
            //添加操作日志
            UserOperateLogUtil.startGame(userId, productId);
        }else if(status!= ClientCode.SUCCESS.getCode()){
            //推送普通设备服务器退出设备通知
            InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
        }
    }

    /**
     * 下爪前的操作处理
     */
    public static void preDownCatch(int productId) {
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        //下机，下爪，设置初始化
        gamingMsg.setInitalization(true);//正在初始化
        gamingMsg.setCatch(true);//已经下爪
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**
     * 添加下爪的结果检测定时器
     */
    public static void addPresentResultCheckTask(int productId) {
        //查询设备缓存信息
        ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        long startGameTime = ProductStartTimeDao.getInstance().loadByProductId(productId);//开始游戏时间
        SchedulerSample.delayed((ProductConfigMsg.resetTime*2) * 1000,
                new PresentResultCheckTask(productRoomMsg.getGamingUserId(), productId, ProductUtil.startGameTime(productId),
                        productRoomMsg.getOnProductTime(), startGameTime));
    }

    /**
     * 处理礼品机返回
     */
    private static void sendPresentRet(int userId, int operateState, int status, int retVal, int productId,
            List<GeneralAwardMsg> awardList){
        JSONObject dataJson = new JSONObject();//内容参数
        dataJson.put("dolRes", retVal);//结果
        dataJson.put("hdlTp", operateState);//操作类型
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));//金币余额
        dataJson.put("devId", productId);//设备ID
        if(awardList!=null && awardList.size()>0) {
            dataJson.put("awdTbln", awardList);//中奖奖励信息
        }
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PRESENT_MACHINE_OPERATION, status, userId,
                dataJson);
    }

    /**
     * 退出设备
     */
    public static void offlineProduct(int productId, int userId, String ip) {
        //游戏信息
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        if(!gamingMsg.isCatch()){
            //发送下爪指令
            //设备操作
            ProductSocketOperateService.presentOperate(productId, ProductOperationEnum.CATCH.getCode(), userId);
        }
        //退出设备
        ProductOperateUtil.offLineProduct(userId, productId);
        //推送客户端
        sendPresentRet(userId, ProductOperationEnum.OFF_LINE.getCode(), ClientCode.SUCCESS.getCode(), 0, productId, null);
        //推送设备服务器下机通知
        InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_OFF_LINE,
                ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                InnerNormalProductWebsocketUtil.initOperateMap(productId, userId, new ArrayList<>()));
    }

    /**
     * 处理获取结果后的设备信息
     */
    public static void dealResultProductMsg(int result, int userId, int productId, int time, long onProductTime,
            List<GeneralAwardMsg> awardList) {
        //查询设备房间信息
        ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
        //游戏信息
        DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
        boolean sendFlag = false;//是否发送消息
        if (roomMsg != null) {
            //推送客户端信息
            int pId = UserOnlineUtil.loadOnlineProduct(userId);
            //重置设备缓存
            resetProductCache(gamingMsg);
            //如果当前是该回合该用户，并且该用户没有在其他设备上，则发送提示，否则不发送
            if (gamingMsg.getTime() == time && roomMsg.getOnProductTime() == onProductTime &&
                    roomMsg.getGamingUserId() == userId) {
                //添加定时下机时间
                SchedulerSample.delayed(PresentConfigMsg.presentOffTime * 1000, new
                        PresentOffLineTask(userId, productId, gamingMsg.getTime(), gamingMsg.getGamingState(),
                        roomMsg.getOnProductTime()));
                //如果玩家在当前设备上
                if (pId == productId) {
                    sendFlag = true;
                }
            }
        }
        //发送信息
        if (sendFlag) {
            //推送客户端
            sendPresentRet(userId, ProductOperationEnum.CATCH_RESULT.getCode(), ClientCode.SUCCESS.getCode(),
                    result, productId, awardList);
        }
    }

    /**
     * 重置设备缓存
     */
    private static void resetProductCache(DollGamingMsg gamingMsg) {
        int productId = gamingMsg.getProductId();//设备ID
        gamingMsg.setInitalization(false);//非初始化
        gamingMsg.setGamingState(
                GamingStateEnum.CHOOSE_LIFE_DEATH.getCode());//选择生死环节
        //更新缓存
        DollGamingMsgDao.getInstance().setCache(productId, gamingMsg);
    }

    /**
     * 检测初始化
     */
    public static void checkInit(int productId, long startGameTime, int userId) {
        if(ProductStartTimeDao.getInstance().loadByProductId(productId)==startGameTime){
            ProductRoomMsg roomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            DollGamingMsg gamingMsg = DollGamingMsgDao.getInstance().loadByProductId(productId);
            if(roomMsg!=null) {
                LogUtil.getLogger().info("礼品机设备{}接收返回结果超时，重新初始化成功------", productId);
                //返回结果
                if(userId>0 && roomMsg.getGamingUserId()==userId){
                    sendPresentRet(userId, ProductOperationEnum.CATCH_RESULT.getCode(),
                            ClientCode.SUCCESS.getCode(), CatchDollResultEnum.LOSE.getCode(), productId, null);
                }
                //重置设备缓存
                resetProductCache(gamingMsg);
            }
        }
    }

    /**
     * 获取到下爪返回结果
     */
    public static void downCatch(InnerProductJsonMapMsg jsonMapMsg) {
        int userId = jsonMapMsg.getUserId();//玩家ID
        int productId = jsonMapMsg.getProductId();//设备ID
        int result = jsonMapMsg.getDataMap().get("catchResult")==null?CatchDollResultEnum.LOSE.getCode():
                (int) jsonMapMsg.getDataMap().get("catchResult");//抓取结果
        ProductGeneralParamsMsg productGeneralParamsMsg = jsonMapMsg.getProductGeneralParamsMsg();//设备信息通用参数
        int time = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getGameTime();//玩家游戏次数
        long onProductTime = productGeneralParamsMsg==null?0:productGeneralParamsMsg.getOnProductTime();//玩家上机时间
        if(userId>0 && productId>0){
            DollAwardCommodityMsg awardMsg = null;
            if(result == CatchDollResultEnum.WIN.getCode()){
                awardMsg = ProductGamingUtil.loadDollAwardMsg(productId);
            }
            //处理获取结果后的设备信息
            dealResultProductMsg(result, userId, productId, time, onProductTime,
                    ProductDealUtil.dealMachineAward(userId, productId, result,
                            EnergyExchangeGetTypeEnum.PRESENT_MACHINE.getCode(), awardMsg));
            //夹到礼品需要添加库存，new设备不做处理
            if (awardMsg!=null) {
                addCatchPresent(userId, productId, awardMsg);
            }
        }else{
            LogUtil.getLogger().info("接收到礼品机下爪返回信息的时候返回的玩家ID{}或者设备ID{}为0-------", userId, productId);
        }
    }

    /**
     * 添加夹到的礼品
     */
    private static void addCatchPresent(int userId, int productId, DollAwardCommodityMsg awardMsg) {
        //添加礼品机奖励
        SchedulerSample.delayed(10,
                new AddPresentAwardTask(productId, userId, awardMsg));
    }
}

