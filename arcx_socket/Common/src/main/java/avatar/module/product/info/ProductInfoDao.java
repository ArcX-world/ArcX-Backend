package avatar.module.product.info;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 设备信息数据接口
 */
public class ProductInfoDao {
    private static final ProductInfoDao instance = new ProductInfoDao();
    public static final ProductInfoDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public ProductInfoEntity loadByProductId(int productId){
        ProductInfoEntity entity = loadCache(productId);
        if(entity==null){
            //查询数据库
            entity = loadDbById(productId);
            if(entity!=null){
                setCache(productId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductInfoEntity loadCache(int productId){
        return (ProductInfoEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_INFO+"_"+productId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int productId, ProductInfoEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_INFO+"_"+productId, entity);
    }


    //=========================db===========================

    /**
     * 根据设备ID查询
     */
    private ProductInfoEntity loadDbById(int productId) {
        return GameData.getDB().get(ProductInfoEntity.class, productId);
    }

}
