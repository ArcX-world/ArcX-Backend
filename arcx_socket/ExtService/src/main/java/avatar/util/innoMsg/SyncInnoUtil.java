package avatar.util.innoMsg;

import avatar.data.product.innoMsg.*;
import avatar.global.enumMsg.product.innoMsg.InnoProductOperateTypeEnum;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.service.innoMsg.InnoReceiveMsgService;
import avatar.task.innoMsg.SyncInnoChangeCoinWeightTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.product.InnoParamsUtil;
import avatar.util.product.InnoProductUtil;
import avatar.util.product.ProductUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 内部自研设备工具类
 */
public class SyncInnoUtil {

    /**
     * 接收信息处理
     */
    public static void dealMsg(String jsonStr) {
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        cachedPool.execute(() -> {
            //加密检测
            if(checkEncode(jsonStr)) {
                int cmd = dealCmd(jsonStr);
                if(cmd>0){
                    Map<String, Object> jsonMap = (Map<String, Object>) JsonUtil.strToMap(jsonStr).get("param");
                    switch (cmd) {
                        case SelfInnoWebsocketInnerCmd.S2P_START_GAME :
                            //开始游戏
                            InnoReceiveMsgService.startGame(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_OPERATE_MSG :
                            //设备操作
                            InnoReceiveMsgService.productOperate(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_START_GAME_GAMING_USER_CHECK :
                            //自研到设备开始游戏玩家校验
                            InnoReceiveMsgService.startGmmeOccopyCheck(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_PRODUCT_MSG:
                            //设备信息
                            InnoReceiveMsgService.productMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_AWARD_LOCK_DEAL:
                            //中奖锁处理信息
                            InnoReceiveMsgService.awardLockDealMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_SETTLEMENT_WINDOW:
                            //结算窗口通知
                            InnoReceiveMsgService.settlementWindowMsg(jsonMap);
                            break;
                        case SelfInnoWebsocketInnerCmd.S2P_AWARD_SCORE_MULTI:
                            //中奖得分倍数
                            InnoReceiveMsgService.awardScoreMulti(jsonMap);
                            break;
                    }
                }else{
                    LogUtil.getLogger().info("收到自研设备服务器的信息：{}，格式解析错误--------", jsonStr);
                }
            }
        });
        cachedPool.shutdown();
    }

    /**
     * 校验加密
     */
    private static boolean checkEncode(String jsonStr) {
        boolean flag = false;
        try{
            if(!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if (jsonMap != null && jsonMap.size() > 0) {
                    Map<String, Object> paramMap = (Map<String, Object>) jsonMap.get("param");//参数内容
                    flag = InnerEnCodeUtil.checkEncode(paramMap);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**
     * 解析指令
     */
    private static int dealCmd(String jsonStr) {
        int cmd = 0;
        try {
            if (!StrUtil.checkEmpty(jsonStr)) {
                Map<String, Object> jsonMap = JsonUtil.strToMap(jsonStr);
                if(jsonMap!=null && jsonMap.size()>0) {
                    //内部操作协议号
                    cmd = (int) jsonMap.get("cmd");
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return cmd;
    }

    /**
     * 发送心跳
     */
    public static void heart(SyncInnoClient client) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_HEART, hostId, new HashMap<>());
        }
    }

    /**
     * 推送开始游戏请求
     */
    public static void startGame(SyncInnoClient client, InnoStartGameMsg startGameMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_START_GAME, hostId,
                    InnoParamsUtil.initStartGameMsg(startGameMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送开始游戏的时候查询不到自研设备服务器对应的信息--------",
                    startGameMsg.getAlias());
        }
    }

    /**
     * 推送结束游戏请求
     */
    public static void endGame(SyncInnoClient client, InnoEndGameMsg endGameMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_END_GAME, hostId,
                    InnoParamsUtil.initEndGameMsg(endGameMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送结束游戏的时候查询不到自研设备服务器对应的信息--------",
                    endGameMsg.getAlias());
        }
    }

    /**
     * 推送设备操作请求
     */
    public static void productOperate(SyncInnoClient client, InnoProductOperateMsg productOperateMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_OPERATE_MSG, hostId,
                    InnoParamsUtil.initProductOperateMsg(productOperateMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送{}操作的时候查询不到自研设备服务器对应的信息--------",
                    productOperateMsg.getAlias(),
                    InnoProductOperateTypeEnum.getNameByCode(productOperateMsg.getInnoProductOperateType()));
        }
    }

    /**
     * 推送开始游戏校验返回
     */
    public static void startGameOccupy(SyncInnoClient client, InnoStartGameOccupyMsg startGameOccupyMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_START_GAME_GAMING_USER_CHECK, hostId,
                    InnoParamsUtil.initStartGameOccupyMsg(startGameOccupyMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送开始游戏校验返回的时候查询不到自研设备服务器对应的信息--------",
                    startGameOccupyMsg.getAlias());
        }
    }

    /**
     * 推送游戏币权重变更请求
     */
    public static void changeCoinWeight(SyncInnoClient client, InnoChangeCoinWeightMsg changeCoinWeightMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_CHANGE_COIN_WEIGHT, hostId,
                    InnoParamsUtil.initChangeCoinWeightMsg(changeCoinWeightMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送权重等级变更的时候查询不到自研设备服务器对应的信息--------",
                    changeCoinWeightMsg.getAlias());
        }
    }

    /**
     * 处理权重
     */
    public static void dealCoinWeight(int preCoinWeight, int userId, int productId) {
        //获取当前权重
        int coinWeight = InnoProductUtil.userCoinWeight(userId, productId);
        if(coinWeight!=preCoinWeight) {
            LogUtil.getLogger().info("推送玩家{}在自研设备{}{}上的权重等级变更，从{}变更到{}--------", userId, productId,
                    ProductUtil.loadProductName(productId), preCoinWeight, coinWeight);
            SchedulerSample.delayed(1, new SyncInnoChangeCoinWeightTask(userId, productId, coinWeight));
        }
    }

    /**
     * 推送自动投币求
     */
    public static void authPushCoin(SyncInnoClient client, InnoAutoPushCoinMsg autoPushCoinMsg) {
        int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
        if(hostId>0) {
            SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_AUTO_PUSH_COIN, hostId,
                    InnoParamsUtil.initAutoPushCoinMsg(autoPushCoinMsg));
        }else{
            LogUtil.getLogger().info("自研设备{}推送{}自动投币的时候查询不到自研设备服务器对应的信息--------",
                    autoPushCoinMsg.getAlias(),autoPushCoinMsg.getIsStart()== YesOrNoEnum.YES.getCode()?"":"取消");
        }
    }
}
