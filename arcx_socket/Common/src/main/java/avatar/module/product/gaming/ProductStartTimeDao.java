package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 设备开始时间数据接口（针对娃娃机和礼品机的提前下机情况）
 */
public class ProductStartTimeDao {
    private static final ProductStartTimeDao instance = new ProductStartTimeDao();
    public static final ProductStartTimeDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID查询
     */
    public long loadByProductId(int productId) {
        //从缓存获取
        return loadCache(productId);
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public long loadCache(int productId){
        Object object = GameData.getCache().get(ProductPrefixMsg.PRODUCT_START_TIME+"_"+productId);
        if(object==null){
            return 0;
        }else{
            return (long) object;
        }
    }

    /**
     * 添加缓存
     */
    public void setCache(int productId, long time){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_START_TIME+"_"+productId, time);
    }
}
