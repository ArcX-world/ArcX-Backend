package avatar.module.product.repair;

import avatar.entity.product.repair.ProductRepairEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.List;

/**
 * 设备报修数据接口
 */
public class ProductRepairDao {
    private static final ProductRepairDao instance = new ProductRepairDao();
    public static final ProductRepairDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID查询报修信息
     */
    public ProductRepairEntity loadByProductId(int productId){
        //从缓存获取
        ProductRepairEntity entity = loadCache(productId);
        if(entity==null){
            //查询数据库
            entity = loadDbByProductId(productId);
            //设置缓存
            if(entity!=null){
                setCache(productId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存信息
     */
    private ProductRepairEntity loadCache(int productId) {
        return (ProductRepairEntity)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId);
    }

    /**
     * 设置缓存
     */
    private void setCache(int productId, ProductRepairEntity entity) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId, entity);
    }

    /**
     * 删除缓存
     */
    private void removeCache(int productId){
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_REPAIR+"_"+productId);
    }

    //=========================db===========================

    /**
     * 根据设备ID查询报修信息
     */
    private ProductRepairEntity loadDbByProductId(int productId) {
        String sql = "select * from product_repair where product_id=? and status=? order by create_time desc";
        return GameData.getDB().get(ProductRepairEntity.class, sql, new Object[]{productId, YesOrNoEnum.NO.getCode()});
    }

    /**
     * 添加
     */
    public boolean insert(ProductRepairEntity entity) {
        return GameData.getDB().insert(entity);
    }

    /**
     * 更新
     */
    public boolean update(ProductRepairEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //删除缓存
            removeCache(entity.getProductId());
        }
        return flag;
    }

    /**
     * 查询列表
     */
    public List<ProductRepairEntity> loadProductList(int productId) {
        String sql = "select * from product_repair where product_id=? and status=? order by create_time desc";
        return GameData.getDB().list(ProductRepairEntity.class, sql, new Object[]{productId, YesOrNoEnum.NO.getCode()});
    }
}
