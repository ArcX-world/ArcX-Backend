package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPaySpecifyCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备付费NA指定权重数据接口
 */
public class InnoNaPaySpecifyCoinWeightDao {
    private static final InnoNaPaySpecifyCoinWeightDao instance = new InnoNaPaySpecifyCoinWeightDao();
    public static final InnoNaPaySpecifyCoinWeightDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnoNaPaySpecifyCoinWeightEntity> loadBySecondType(int secondType) {
        //从缓存获取
        List<InnoNaPaySpecifyCoinWeightEntity> list = loadCache(secondType);
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
    private List<InnoNaPaySpecifyCoinWeightEntity> loadCache(int secondType){
        return (List<InnoNaPaySpecifyCoinWeightEntity>) GameData.getCache().get(
                ProductPrefixMsg.INNO_NA_PAY_SPECIFY_COIN_WEIGHT+"_"+secondType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int secondType, List<InnoNaPaySpecifyCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_SPECIFY_COIN_WEIGHT+"_"+secondType, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<InnoNaPaySpecifyCoinWeightEntity> loadDbBySecondType(int secondType) {
        String sql = "select * from inno_na_pay_specify_coin_weight where second_type=? order by level";
        return GameData.getDB().list(InnoNaPaySpecifyCoinWeightEntity.class, sql, new Object[]{secondType});
    }
}
