package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductSpecifyCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备指定游戏币权重数据接口
 */
public class InnoProductSpecifyCoinWeightDao {
    private static final InnoProductSpecifyCoinWeightDao instance = new InnoProductSpecifyCoinWeightDao();
    public static final InnoProductSpecifyCoinWeightDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnoProductSpecifyCoinWeightEntity> loadBySecondType(int secondType) {
        //从缓存获取
        List<InnoProductSpecifyCoinWeightEntity> list = loadCache(secondType);
        if(list==null){
            //从数据库查询
            list = loadDbBySecondType(secondType);
            if(list==null){
                list = new ArrayList<>();
            }
            //设置缓存
            setCache(secondType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<InnoProductSpecifyCoinWeightEntity> loadCache(int secondType){
        return (List<InnoProductSpecifyCoinWeightEntity>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_SPECIFY_COIN_WEIGHT+"_"+secondType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int secondType, List<InnoProductSpecifyCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_SPECIFY_COIN_WEIGHT+"_"+secondType, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<InnoProductSpecifyCoinWeightEntity> loadDbBySecondType(int secondType) {
        String sql = "select * from inno_product_specify_coin_weight where second_type=? order by level";
        return GameData.getDB().list(InnoProductSpecifyCoinWeightEntity.class, sql, new Object[]{secondType});
    }
}
