package avatar.module.product.innoNaPay;

import avatar.entity.product.innoNaPay.InnoNaPayMoneyConfigEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.HashMap;

/**
 * 自研设备付费NA金额配置数据接口
 */
public class InnoNaPayMoneyConfigDao {
    private static final InnoNaPayMoneyConfigDao instance = new InnoNaPayMoneyConfigDao();
    public static final InnoNaPayMoneyConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public InnoNaPayMoneyConfigEntity loadMsg() {
        //从缓存获取
        InnoNaPayMoneyConfigEntity entity = loadCache();
        if(entity==null){
            //从数据库查询
            entity = loadDbAll();
            if(entity!=null) {
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
    private InnoNaPayMoneyConfigEntity loadCache(){
        return (InnoNaPayMoneyConfigEntity) GameData.getCache().get(ProductPrefixMsg.INNO_NA_PAY_MONEY_CONFIG);
    }

    /**
     * 添加缓存
     */
    private void setCache(InnoNaPayMoneyConfigEntity entity){
        GameData.getCache().set(ProductPrefixMsg.INNO_NA_PAY_MONEY_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private InnoNaPayMoneyConfigEntity loadDbAll() {
        String sql = SqlUtil.getSql("inno_na_pay_money_config", new HashMap<>()).toString();
        return GameData.getDB().get(InnoNaPayMoneyConfigEntity.class, sql, new Object[]{});
    }
}
