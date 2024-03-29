package avatar.service.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.enumMsg.product.info.ProductStatusEnum;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.module.product.gaming.ProductRoomDao;
import avatar.module.product.info.ProductInfoDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.task.innoMsg.SyncInnoAutoPushCoinTask;
import avatar.task.innoMsg.SyncInnoEndGameTask;
import avatar.task.innoMsg.SyncInnoProductOperateTask;
import avatar.task.innoMsg.SyncInnoStartGameTask;
import avatar.task.product.operate.ProductPushCoinTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.InnoSendWebsocketUtil;
import avatar.util.innoMsg.SyncInnoClient;
import avatar.util.innoMsg.SyncInnoConnectUtil;
import avatar.util.innoMsg.SyncInnoOperateUtil;
import avatar.util.product.*;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * 自研推币机接口实现类
 */
public class CoinPusherInnoService {
    /**
     * 检测操作是否正常
     */
    public static int checkOperate(int userId, int coinMulti, int operateState, ProductRoomMsg productRoomMsg,
            boolean unlockFlag) {
        int productId = productRoomMsg.getProductId();//设备ID
        //查询设备信息
        ProductInfoEntity entity = ProductInfoDao.getInstance().loadByProductId(productId);
        int status = ClientCode.SUCCESS.getCode();//成功
        if(operateState== ProductOperationEnum.START_GAME.getCode() && entity.getStatus()!= ProductStatusEnum.NORMAL.getCode()){
            status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常
        }else if(operateState== ProductOperationEnum.START_GAME.getCode() && productRoomMsg.getGamingUserId()>0){
            status = ClientCode.PRODUCT_OCCUPY.getCode();//设备占用中
        } else if(productRoomMsg.getGamingUserId()!=userId && operateState!= ProductOperationEnum.START_GAME.getCode()){
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();//设备玩家不匹配
            LogUtil.getLogger().error("设备{}的游戏玩家{}和上机玩家{}不匹配--------", productId,
                    productRoomMsg.getGamingUserId(), userId);
        } else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                !ProductUtil.isEnoughCost(userId, productId, coinMulti) && !InnoProductUtil.isInnoFreeCoin(productId)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        } else if(!unlockFlag && InnoProductUtil.isCoinMultiLowerLimit(userId, coinMulti, productId)){
            status = ClientCode.MULTI_LOCK.getCode();//低倍率限制
        } else if(operateState== ProductOperationEnum.PUSH_COIN.getCode() && !ProductUtil.isNormalProduct(productId)){
            status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常，禁止投币
        } else if(ProductUtil.isOffLine(operateState) && ProductUtil.isInnoProduct(productId) &&
                ProductGamingUtil.isProductAwardLock(productId)){
            LogUtil.getLogger().error("设备{}中奖锁定中，不允许玩家{}退出设备-------", productId, userId);
            status = ClientCode.AWARD_LOCK.getCode();//中奖锁定中
        } else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                UserOnlineUtil.isNoStreeSettlementGaming(userId)){
            status = ClientCode.GAMING_FORBID.getCode();//游戏中，请等待游戏结束
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            JSONObject dataJson = new JSONObject();
            dataJson.put("flAmt", 0);//得到币
            dataJson.put("hdlTp", operateState);//发送的操作
            dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));//玩家余额
            dataJson.put("devId", productId);//设备ID
            if(status==ClientCode.PRODUCT_EXCEPTION.getCode()){
                //设备异常禁止投币，优化前端展示，返回成功
                SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION,
                        ClientCode.SUCCESS.getCode(), userId, dataJson);
            }else if(status!= ClientCode.AWARD_LOCK.getCode()){
                SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId, dataJson);
            }
        }
        //处理非游戏中玩家信息
        dealNoGamingUser(userId, productId, status);
        return status;
    }

    /**
     * 处理非游戏中玩家信息
     */
    private static void dealNoGamingUser(int userId, int productId, int status) {
        if(status== ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode() && ProductUtil.isInnoProduct(productId)){
            String productIp = ProductUtil.productIp(productId);//设备IP
            int productPort = ProductUtil.productSocketPort(productId);//设备端口
            String alias = ProductUtil.loadProductAlias(productId);//设备号
            //推送自研设备退出
            SchedulerSample.delayed(1, new SyncInnoEndGameTask(
                    productIp, productPort,
                    InnoParamsUtil.initInnoEndGameMsg(productId, alias, userId)));
            //删除在线信息
            UserOnlineMsgDao.getInstance().delete(userId, productId);
            //删除socket信息
            ProductSocketUtil.dealOffLineSession(userId, productId);
        }
    }

    /**
     * 推币机操作
     */
    public static void coinPusherOperation(int productId, int operateState, int userId) {
        int status = ClientCode.SUCCESS.getCode();//成功
        String productIp = ProductUtil.productIp(productId);//设备IP
        int productPort = ProductUtil.productSocketPort(productId);//设备端口
        String alias = ProductUtil.loadProductAlias(productId);//设备号
        String linkMsg = SyncInnoConnectUtil.linkMsg(productIp, productPort);//链接信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(productIp, productPort, linkMsg);
        if (client != null && client.isOpen()) {
            //查询设备缓存信息
            ProductRoomMsg productRoomMsg = ProductRoomDao.getInstance().loadByProductId(productId);
            if(operateState== ProductOperationEnum.START_GAME.getCode()){
                //开始游戏
                SchedulerSample.delayed(1, new SyncInnoStartGameTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoStartGameMsg(productId, alias, userId)));
            }else if(operateState== ProductOperationEnum.PUSH_COIN.getCode()){
                //投币操作
                boolean innoFreeLink = InnoProductUtil.isInnoFreeCoin(productId);//是否自研设备免费环节
                status = CoinPusherInnerReceiveDealUtil.pushCoin(userId, productId, innoFreeLink);
                if(ParamsUtil.isSuccess(status)) {
                    //推送自研设备操作投币
                    SchedulerSample.delayed(1, new SyncInnoProductOperateTask(
                            productIp, productPort,
                            InnoParamsUtil.initInnoProductOperateMsg(productId, alias, userId,
                                    productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.PUSH_COIN.getCode(),
                                    innoFreeLink)));
                    //推送投币结果
                    InnoSendWebsocketUtil.sendWebsocketMsg(userId,
                            ProductOperationEnum.PUSH_COIN.getCode(), status, 0,
                            productId);
                    //添加投币处理
                    SchedulerSample.delayed(10, new ProductPushCoinTask(productId, userId,
                            ProductUtil.productCost(productId)));
                }
            }else if(operateState== ProductOperationEnum.ROCK.getCode()){
                //雨刷
                //推送自研设备操作雨刷
                SchedulerSample.delayed(1, new SyncInnoProductOperateTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoProductOperateMsg(productId, alias, userId,
                                productRoomMsg.getOnProductTime(), InnoProductOperateTypeEnum.WIPER.getCode(), false)));
            }else if(operateState== ProductOperationEnum.OFF_LINE.getCode()){
                //退出设备
                CoinPusherInnerReceiveDealUtil.offlineProduct(productId, userId);
                //自研取消自动投币
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
                //推送自研设备退出
                SchedulerSample.delayed(1, new SyncInnoEndGameTask(
                        productIp, productPort,
                        InnoParamsUtil.initInnoEndGameMsg(productId, alias, userId)));
            }else if(operateState== ProductOperationEnum.AUTO_PUSH_COIN.getCode()){
                //自动投币
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.YES.getCode()));
            }else if(operateState== ProductOperationEnum.CANCEL_AUTO_PUSH_COIN.getCode()){
                //取消自动投币
                SchedulerSample.delayed(1, new SyncInnoAutoPushCoinTask(userId, productId, YesOrNoEnum.NO.getCode()));
            }
        }else{
            LogUtil.getLogger().info("自研设备{}异常-------", productId);
            status = ClientCode.PRODUCT_EXCEPTION.getCode();//设备异常
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId, new JSONObject());
        }
    }
}
