package avatar.util.innoMsg;

import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.util.product.CoinPusherInnerReceiveDealUtil;
import avatar.util.product.ProductUtil;

/**
 * 自研设备推送websocket信息
 */
public class InnoSendWebsocketUtil {
    /**
     * 推送websocket信息
     * @param userId 玩家ID
     * @param operateType 设备操作类型
     * @param status 错误码
     * @param getCoin 获得币数量
     * @param productId 设备ID
     */
    public static void sendWebsocketMsg(int userId, int operateType, int status, int getCoin, int productId) {
        if(ProductUtil.loadProductType(productId) == ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            //推币机
            pushCoinMsg(userId, operateType, status, getCoin, productId);
        }
    }

    /**
     * 推送推币机信息
     */
    private static void pushCoinMsg(int userId, int operateType, int status, int getCoin, int productId) {
        //推送客户端
        CoinPusherInnerReceiveDealUtil.sendCoinPusherRet(userId, operateType, status, getCoin, productId);
    }
}
