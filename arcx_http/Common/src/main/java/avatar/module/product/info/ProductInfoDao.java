package avatar.module.product.info;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;

import java.util.Collections;
import java.util.List;

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

    /**
     * 更新
     */
    public boolean update(ProductInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getId(), entity);
            //重置设备类型折叠设备列表缓存
            ProductTypeFordingListDao.getInstance().resetCache(entity.getProductType());
            //重置设备二级分类折叠设备列表
            ProductSecondTypeFoldingListDao.getInstance().resetCache(entity.getSecondType());
            //重置设备二级类型设备列表
            ProductSecondTypeProductListDao.getInstance().resetCache(entity.getSecondType());
            //重置设备群组缓存
            ProductGroupListDao.getInstance().resetCache(entity.getLiveUrl());
            //重置设备号列表缓存
            ProductAliasListDao.getInstance().resetCache();
        }
        return flag;
    }

    /**
     * 根据分页查询设备
     */
    public List<ProductInfoEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList(" FIELD (status,1,0,2),product_type,sequence,second_sequence,create_time ");
        StringBuffer sb = SqlUtil.loadList("product_info", orderList);
        return GameData.getDB().list(ProductInfoEntity.class, sb.toString(), new Object[]{});
    }

    /**
     * 根据设备号查询
     */
    public ProductInfoEntity loadDbByAlias(String alias) {
        String sql = "select * from product_info where alias=?";
        return GameData.getDB().get(ProductInfoEntity.class, sql, new Object[]{alias});
    }
}
