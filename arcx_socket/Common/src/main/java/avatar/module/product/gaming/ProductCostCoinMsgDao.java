package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductCostCoinMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 设备消费游戏币信息数据接口
 */
public class ProductCostCoinMsgDao {
    private static final ProductCostCoinMsgDao instance = new ProductCostCoinMsgDao();
    public static final ProductCostCoinMsgDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public ProductCostCoinMsg loadByProductId(int productId) {
        //从缓存获取
        ProductCostCoinMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initProductCostCoinMsg(productId);
            //设置店铺的缓存信息
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取设备房间信息
     */
    private ProductCostCoinMsg loadCache(int productId) {
        return (ProductCostCoinMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_COST_COIN_MSG+"_"+productId);
    }

    /**
     * 设置设备房间信息
     */
    public void setCache(int productId, ProductCostCoinMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_COST_COIN_MSG+"_"+productId, msg);
    }
}
