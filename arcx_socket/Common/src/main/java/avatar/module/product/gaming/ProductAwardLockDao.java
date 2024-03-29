package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.ProductAwardLockMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 设备中奖锁定信息数据接口
 */
public class ProductAwardLockDao {
    private static final ProductAwardLockDao instance = new ProductAwardLockDao();
    public static final ProductAwardLockDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public ProductAwardLockMsg loadByProductId(int productId) {
        //从缓存获取
        ProductAwardLockMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initProductAwardLockMsg(productId);
            //设置设备的缓存信息
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取设备房间信息
     */
    private ProductAwardLockMsg loadCache(int productId) {
        return (ProductAwardLockMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_LOCK_MSG+"_"+productId);
    }

    /**
     * 设置设备房间信息
     */
    public void setCache(int productId, ProductAwardLockMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_LOCK_MSG+"_"+productId, msg);
    }
}
