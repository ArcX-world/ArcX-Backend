package avatar.service.product;

import avatar.util.product.ProductUtil;

/**
 * 设备socket操作
 */
public class ProductSocketOperateService {
    /**
     * 娃娃机操作
     */
    public static void catchDollOperate(int productId, int operateState, int userId){
        CatchDollService.catchDollOperation(productId, operateState, userId);
    }

    /**
     * 推币机操作
     */
    public static void coinPusherOperate(int productId, int operateState, int userId){
        if(ProductUtil.isInnoProduct(productId)) {
            //自研设备
            CoinPusherInnoService.coinPusherOperation(productId, operateState, userId);
        }else{
            //普通设备
            CoinPusherNormalService.coinPusherOperation(productId, operateState, userId);
        }
    }

    /**
     * 礼品机操作
     */
    public static void presentOperate(int productId, int operateState, int userId){
        PresentService.presentOperation(productId, operateState, userId);
    }
}
