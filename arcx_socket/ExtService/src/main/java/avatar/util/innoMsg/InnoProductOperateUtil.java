package avatar.util.innoMsg;

import avatar.data.product.innoMsg.InnoReceiveProductOperateMsg;
import avatar.data.product.innoMsg.InnoReceiveStartGameMsg;
import avatar.global.enumMsg.product.info.ProductTypeEnum;
import avatar.util.product.ProductUtil;

/**
 * 自研设备操作工具类
 */
public class InnoProductOperateUtil {
    /**
     * 开始游戏
     */
    public static void startGame(InnoReceiveStartGameMsg startGameMsg, int productId) {
        int productType = ProductUtil.loadProductType(productId);//设备类型
        if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            //推币机
            CoinPusherInnoProductOperateUtil.startGame(startGameMsg, productId);
        }
    }

    /**
     * 设备操作
     */
    public static void productOperate(InnoReceiveProductOperateMsg productOperateMsg, int productId) {
        int productType = ProductUtil.loadProductType(productId);//设备类型
        if(productType== ProductTypeEnum.PUSH_COIN_MACHINE.getCode()){
            //推币机
            CoinPusherInnoProductOperateUtil.productOperate(productOperateMsg, productId);
        }
    }


}
