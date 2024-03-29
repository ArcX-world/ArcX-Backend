package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.InnoHighLevelCoinWeightEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备高等级NA权重数据接口
 */
public class InnoHighLevelCoinWeightDao {
    private static final InnoHighLevelCoinWeightDao instance = new InnoHighLevelCoinWeightDao();
    public static final InnoHighLevelCoinWeightDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnoHighLevelCoinWeightEntity> loadByMsg(int secondType, int payFlag) {
        //从缓存获取
        List<InnoHighLevelCoinWeightEntity> list = loadCache(secondType, payFlag);
        if(list==null){
            //从数据库查询
            list = loadDbByMsg(secondType, payFlag);
            //设置缓存
            setCache(secondType, payFlag, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<InnoHighLevelCoinWeightEntity> loadCache(int secondType, int payFlag){
        return (List<InnoHighLevelCoinWeightEntity>) GameData.getCache().get(
                ProductPrefixMsg.INNO_HIGH_LEVEL_NA+"_"+secondType+"_"+payFlag);
    }

    /**
     * 添加缓存
     */
    private void setCache(int secondType, int payFlag, List<InnoHighLevelCoinWeightEntity> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_HIGH_LEVEL_NA+"_"+secondType+"_"+payFlag, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<InnoHighLevelCoinWeightEntity> loadDbByMsg(int secondType, int payFlag) {
        String sql = "select * from inno_high_level_coin_weight where second_type=? and pay_flag=? " +
                " order by level,na_num";
        List<InnoHighLevelCoinWeightEntity> list =  GameData.getDB().list(
                InnoHighLevelCoinWeightEntity.class, sql, new Object[]{secondType, payFlag});
        return list==null?new ArrayList<>():list;
    }

}
