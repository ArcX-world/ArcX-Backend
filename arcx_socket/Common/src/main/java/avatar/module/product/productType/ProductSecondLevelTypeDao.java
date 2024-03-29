package avatar.module.product.productType;

import avatar.entity.product.productType.ProductSecondLevelTypeEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 设备二级分类数据接口
 */
public class ProductSecondLevelTypeDao {
    private static final ProductSecondLevelTypeDao instance = new ProductSecondLevelTypeDao();
    public static final ProductSecondLevelTypeDao getInstance(){
        return instance;
    }

    /**
     * 根据二级ID查询设信息
     */
    public ProductSecondLevelTypeEntity loadBySecondType(int secondType) {
        //从缓存获取
        ProductSecondLevelTypeEntity entity = loadCache(secondType);
        if(entity==null){
            //查询数据库
            entity = loadDbBySecondType(secondType);
            //添加缓存
            if(entity!=null){
                setCache(secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public ProductSecondLevelTypeEntity loadCache(int secondType){
        return (ProductSecondLevelTypeEntity)
                        GameData.getCache().get(ProductPrefixMsg.PRODUCT_SECOND_LEVEL_TYPE+"_"+secondType);
    }

    /**
     * 添加缓存
     */
    public void setCache(int secondType, ProductSecondLevelTypeEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SECOND_LEVEL_TYPE+"_"+secondType, entity);
    }

    //=========================db===========================

    /**
     * 根据二级分类查询分类信息
     */
    private ProductSecondLevelTypeEntity loadDbBySecondType(int secondType){
        String sql = "select * from product_second_level_type where id=?";
        return GameData.getDB().get(ProductSecondLevelTypeEntity.class, sql, new Object[]{secondType});
    }
}
