package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoProductCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自研设备游戏币权重数据接口
 */
public class InnoProductCoinWeightDao {
    private static final InnoProductCoinWeightDao instance = new InnoProductCoinWeightDao();
    public static final InnoProductCoinWeightDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnoProductCoinWeightEntity> loadMsg() {
        //从缓存获取
        List<InnoProductCoinWeightEntity> list = loadCache();
        if(list==null){
            //从数据库查询
            list = loadDbAll();
            if(list==null){
                list = new ArrayList<>();
            }
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<InnoProductCoinWeightEntity> loadCache(){
        return (List<InnoProductCoinWeightEntity>) GameData.getCache().get(ProductPrefixMsg.INNO_PRODUCT_COIN_WEIGHT);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<InnoProductCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_COIN_WEIGHT, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<InnoProductCoinWeightEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList("level");//排序
        String sql = SqlUtil.loadList("inno_product_coin_weight", orderList).toString();
        return GameData.getDB().list(InnoProductCoinWeightEntity.class, sql, new Object[]{});
    }
}
