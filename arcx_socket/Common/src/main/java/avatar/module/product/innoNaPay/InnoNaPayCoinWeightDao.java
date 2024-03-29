package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPayCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自研设备付费NA权重数据接口
 */
public class InnoNaPayCoinWeightDao {
    private static final InnoNaPayCoinWeightDao instance = new InnoNaPayCoinWeightDao();
    public static final InnoNaPayCoinWeightDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnoNaPayCoinWeightEntity> loadMsg() {
        //从缓存获取
        List<InnoNaPayCoinWeightEntity> list = loadCache();
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
    private List<InnoNaPayCoinWeightEntity> loadCache(){
        return (List<InnoNaPayCoinWeightEntity>) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_COIN_WEIGHT);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<InnoNaPayCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_COIN_WEIGHT, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<InnoNaPayCoinWeightEntity> loadDbAll() {
        List<String> orderList = Collections.singletonList("level");//排序
        String sql = SqlUtil.loadList("inno_na_pay_coin_weight", orderList).toString();
        return GameData.getDB().list(InnoNaPayCoinWeightEntity.class, sql, new Object[]{});
    }
}
