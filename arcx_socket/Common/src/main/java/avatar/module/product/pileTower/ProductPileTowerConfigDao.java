package avatar.module.product.pileTower;

import avatar.entity.product.pileTower.ProductPileTowerConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 设备炼金塔堆塔配置数据接口
 */
public class ProductPileTowerConfigDao {
    private static final ProductPileTowerConfigDao instance = new ProductPileTowerConfigDao();
    public static final ProductPileTowerConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public ProductPileTowerConfigEntity loadMsg() {
        //从缓存获取
        ProductPileTowerConfigEntity entity = loadCache();
        if(entity==null){
            //查询数据库
            entity = loadDbMsg();
            if(entity!=null){
                //设置缓存
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductPileTowerConfigEntity loadCache(){
        return (ProductPileTowerConfigEntity) GameData.getCache().get(ProductPrefixMsg.PRODUCT_PILE_TOWER_CONFIG);
    }

    /**
     * 添加缓存
     */
    private void setCache(ProductPileTowerConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_PILE_TOWER_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private ProductPileTowerConfigEntity loadDbMsg() {
        String sql = "select * from product_pile_tower_config";
        return GameData.getDB().get(ProductPileTowerConfigEntity.class, sql, new Object[]{});
    }
}
