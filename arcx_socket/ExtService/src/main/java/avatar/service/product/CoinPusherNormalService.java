package avatar.service.product;

import avatar.global.code.basicConfig.CoinPusherConfigMsg;
import avatar.global.enumMsg.basic.commodity.CommodityTypeEnum;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.CoinPusherInnerReceiveDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserBalanceUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通推币机操作实现类
 */
public class CoinPusherNormalService {
    /**
     * 推币机操作
     */
    public static void coinPusherOperation(int productId, int operateState, int userId) {
        int status = ClientCode.SUCCESS.getCode();//成功
        if(operateState== ProductOperationEnum.START_GAME.getCode()){
            //开始游戏
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_START_GAME,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId,
                            StrUtil.strToStrList(CoinPusherConfigMsg.startGameOperate)));
        }else if(operateState== ProductOperationEnum.PUSH_COIN.getCode()){
            //投币操作
            status = CoinPusherInnerReceiveDealUtil.pushCoin(userId, productId, false);
            if(ParamsUtil.isSuccess(status)) {
                //推送设备操作投币
                status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_OPERATE,
                        ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                        InnerNormalProductWebsocketUtil.initProductOperateMap(productId, userId,
                                StrUtil.strToStrList(CoinPusherConfigMsg.pushCoinOperate)));
                //推送投币结果
                sendPushCoinRet(userId, productId);
            }
        }else if(operateState== ProductOperationEnum.GET_COIN.getCode()){
            //获得币
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_GET_COIN,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initGetCoinOperateMap(productId, userId,
                            StrUtil.strToStrList(CoinPusherConfigMsg.getCoinOperate)));
        }else if(operateState== ProductOperationEnum.OFF_LINE.getCode()){
            //退出设备
            CoinPusherInnerReceiveDealUtil.offlineProduct(productId, userId);
        }else{
            //其他操作
            List<String> operateList = new ArrayList<>();
            operateList.add(operateState+"");
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_OPERATE,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initProductOperateMap(productId, userId,
                            operateList));
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, status, userId, new JSONObject());
        }
    }

    /**
     * 推送投币返回
     */
    private static void sendPushCoinRet(int userId, int productId) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("hdlTp", ProductOperationEnum.PUSH_COIN.getCode());//发送的操作
        dataJson.put("gdAmt", UserBalanceUtil.getUserBalance(userId, CommodityTypeEnum.GOLD_COIN.getCode()));//玩家余额
        dataJson.put("devId", productId);//设备ID
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_COIN_PUSHER_OPERATION, ClientCode.SUCCESS.getCode(), userId, dataJson);
    }
}
