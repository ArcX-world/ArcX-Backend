package avatar.module.product.repair;

import avatar.entity.product.repair.ProductRepairConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**
 * 设备维护配置信息
 */
public class ProductRepairConfigDao {
    private static final ProductRepairConfigDao instance = new ProductRepairConfigDao();
    public static final ProductRepairConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
        public ProductRepairConfigEntity loadMsg(){
        ProductRepairConfigEntity entity = loadCache();
        if(entity==null){
            //查询数据库
            entity = loadDbMsg();
            if(entity!=null){
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private ProductRepairConfigEntity loadCache(){
        return (ProductRepairConfigEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_REPAIR_CONFIG);
    }

    /**
     * 添加缓存
     */
    private void setCache(ProductRepairConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_REPAIR_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private ProductRepairConfigEntity loadDbMsg() {
        String sql = SqlUtil.getSql("product_repair_config", new HashMap<>()).toString();
        return GameData.getDB().get(ProductRepairConfigEntity.class, sql, new Object[]{});
    }
}
