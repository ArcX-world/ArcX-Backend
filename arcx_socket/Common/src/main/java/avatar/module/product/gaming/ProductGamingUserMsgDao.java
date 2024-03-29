package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductGamingUserMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 设备游戏中玩家信息数据接口
 */
public class ProductGamingUserMsgDao {
    private static final ProductGamingUserMsgDao instance = new ProductGamingUserMsgDao();
    public static final ProductGamingUserMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public ProductGamingUserMsg loadByProductId(int productId){
        ProductGamingUserMsg msg = loadCache(productId);
        if(msg==null){
            //初始化信息
            msg = ProductGamingUtil.initProductGamingUserMsg(productId);
            //设置缓存
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductGamingUserMsg loadCache(int productId){
        return (ProductGamingUserMsg)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_GAMING_USER+"_"+productId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int productId, ProductGamingUserMsg msg){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_GAMING_USER+"_"+productId, msg);
    }


}
