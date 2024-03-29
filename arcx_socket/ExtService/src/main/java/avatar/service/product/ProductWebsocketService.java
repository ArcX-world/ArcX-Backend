package avatar.service.product;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.global.Config;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.product.gaming.ProductGamingUserMsgDao;
import avatar.task.product.general.PushJoinProductTask;
import avatar.task.product.general.RefreshProductMsgTask;
import avatar.util.LogUtil;
import avatar.util.innoMsg.SendInnoInnerMsgUtil;
import avatar.util.log.UserOperateLogUtil;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.JsonUtil;
import avatar.util.trigger.SchedulerSample;
import avatar.util.user.UserOnlineUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备websocket接口实现
 */
public class ProductWebsocketService {
    /**
     * 进入设备
     */
    public static void joinProduct(int userId, int productId, boolean joinFlag) {
        //刷新房间信息
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
        if(userId>0) {
            LogUtil.getLogger().info("玩家{}进入设备{}-{}------", userId, productId, ProductUtil.loadProductName(productId));
            //添加在线信息
            UserOnlineUtil.joinProductMsg(userId, productId, Config.getInstance().getLocalAddr(),
                    Config.getInstance().getWebSocketPort());
            if (joinFlag) {
                //推送进入设备通知
                SchedulerSample.delayed(5, new PushJoinProductTask(productId, userId));
            }
            //如果当前有游戏中玩家，检测在线信息
            productMsgCheck(productId);
            //添加操作日志
            UserOperateLogUtil.joinProduct(userId, productId);
        }
    }

    /**
     * 如果有在线玩家，则验证在线信息
     */
    private static void productMsgCheck(int productId) {
        //查询设备游戏中玩家信息
        ProductGamingUserMsg msg = ProductGamingUserMsgDao.getInstance().loadByProductId(productId);
        if(msg.getServerSideType()>0 && msg.getUserId()>0){
            //有玩家游戏中，检测
            if(ProductUtil.isInnoProduct(productId)){
                //自研设备
                SendInnoInnerMsgUtil.sendClientMsg(productId, SelfInnoWebsocketInnerCmd.P2S_PRODUCT_MSG, initProductMsgMap(msg));
            }else {
                //普通设备
                InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_PRODUCT_MSG,
                        ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                        InnerNormalProductWebsocketUtil.initProductMsgOperateMap(msg));
            }
        }
    }

    /**
     * 填充设备信息参数
     */
    private static Map<Object, Object> initProductMsgMap(ProductGamingUserMsg msg) {
        Map<Object, Object> paramsMap = new HashMap<>();
        int productId = msg.getProductId();//设备ID
        paramsMap.put("alias", ProductUtil.loadProductAlias(productId));//设备号
        paramsMap.put("userId", msg.getUserId());//玩家ID
        paramsMap.put("userName", msg.getUserName());//玩家昵称
        paramsMap.put("imgUrl", msg.getImgUrl());//玩家头像
        paramsMap.put("serverSideType", msg.getServerSideType());//服务端类型
        return paramsMap;
    }

    /**
     * 退出设备
     */
    public static void exitProduct(int userId, int productId) {
        LogUtil.getLogger().info("玩家{}退出设备{}-{}------", userId, productId, ProductUtil.loadProductName(productId));
        //更新在线信息
        UserOnlineUtil.exitProductMsg(userId, productId);
        //刷新房间信息
        SchedulerSample.delayed(5, new RefreshProductMsgTask(productId));
        //添加操作日志
        UserOperateLogUtil.existProduct(userId, productId);
    }

    /**
     * 自研设备龙珠信息
     */
    public static void innoDragonMsg(int userId) {
        JSONObject dataJson = new JSONObject();
        //查询玩家龙珠玛丽机信息
        DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
        dataJson.put("dgbAmt", entity==null?0:entity.getDragonNum());//龙珠数量
        LogUtil.getLogger().info("推送玩家{}自研设备龙珠信息通知的信息：{}--------", userId, JsonUtil.mapToJson(dataJson));
        //推送前端
        SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_INNO_DRAGON_MSG,
                ClientCode.SUCCESS.getCode(), userId, dataJson);
    }

}
