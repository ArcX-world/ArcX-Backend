package avatar.service.product;

import avatar.data.product.gamingMsg.ProductRoomMsg;
import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.code.basicConfig.PresentConfigMsg;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.enumMsg.product.info.ProductOperationEnum;
import avatar.global.linkMsg.websocket.WebSocketCmd;
import avatar.global.linkMsg.websocket.WebsocketInnerCmd;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.normalProduct.InnerNormalProductWebsocketUtil;
import avatar.util.product.PresentInnerReceiveDealUtil;
import avatar.util.product.ProductUtil;
import avatar.util.sendMsg.SendWebsocketMsgUtil;
import avatar.util.system.StrUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * 礼品机操作实现类
 */
public class PresentService {
    /**
     * 检测操作是否正常
     */
    public static int checkOperate(int operateState, int userId, ProductRoomMsg productRoomMsg) {
        int productId = productRoomMsg.getProductId();//设备ID
        int status = ClientCode.SUCCESS.getCode();//成功
        if(operateState== ProductOperationEnum.START_GAME.getCode()
                && productRoomMsg.getGamingUserId()>0
                && (ProductUtil.startGameTime(productId)==0 || productRoomMsg.getGamingUserId()!=userId)){
            status = ClientCode.PRODUCT_OCCUPY.getCode();//设备占用中
        } else if(productRoomMsg.getGamingUserId()!=userId && operateState!= ProductOperationEnum.START_GAME.getCode()){
            status = ClientCode.PRODUCT_GAMING_USER_NOT_FIT.getCode();//设备玩家不匹配
        }else if(operateState== ProductOperationEnum.START_GAME.getCode() &&
                !ProductUtil.isEnoughCost(userId, productId, 0)){
            status = ClientCode.BALANCE_NO_ENOUGH.getCode();//余额不足
        }
        if(status!= ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PRESENT_MACHINE_OPERATION, status, userId, new JSONObject());
        }
        return status;
    }

    /**
     * 礼品机操作
     */
    public static void presentOperation(int productId, int operateState, int userId) {
        int status = ClientCode.SUCCESS.getCode();//成功
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        if(operateState==ProductOperationEnum.START_GAME.getCode()){
            //开始游戏
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_START_GAME,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initOperateMap(productId, userId,
                            StrUtil.strToStrList(PresentConfigMsg.startGameOperate)));
        }else if(operateState==ProductOperationEnum.ROCK.getCode()){
            //下爪，用摇摆的操作指令
            //发送下爪指令
            status = InnerNormalProductWebsocketUtil.sendOperateMsg(WebsocketInnerCmd.C2S_DOWN_CATCH,
                    ProductUtil.productIp(productId), ProductUtil.productSocketPort(productId),
                    InnerNormalProductWebsocketUtil.initDownCatchOperateMap(productId, userId,
                            StrUtil.strToStrList(PresentConfigMsg.downClawOperate)));
            if(status==ClientCode.SUCCESS.getCode()) {
                //做下爪处理
                PresentInnerReceiveDealUtil.preDownCatch(productId);
                //添加下爪操作检测定时器
                PresentInnerReceiveDealUtil.addPresentResultCheckTask(productId);
            }
        }else if(operateState==ProductOperationEnum.OFF_LINE.getCode()){
            //退出设备
            PresentInnerReceiveDealUtil.offlineProduct(productId, userId, productInfoEntity.getIp());
        }
        if(status!=ClientCode.SUCCESS.getCode()){
            SendWebsocketMsgUtil.sendByUserId(WebSocketCmd.S2C_PRESENT_MACHINE_OPERATION, status, userId, new JSONObject());
        }
    }
}
