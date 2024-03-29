package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**
 * 充值道具配置数据接口
 */
public class RechargePropertyConfigDao {
    private static final RechargePropertyConfigDao instance = new RechargePropertyConfigDao();
    public static final RechargePropertyConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public RechargePropertyConfigEntity loadMsg() {
        //从缓存获取
        RechargePropertyConfigEntity entity = loadCache();
        if(entity==null){
            entity = loadDbMsg();
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
    private RechargePropertyConfigEntity loadCache(){
        return (RechargePropertyConfigEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_CONFIG);
    }

    /**
     * 添加缓存
     */
    private void setCache(RechargePropertyConfigEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_CONFIG, entity);
    }


    //=========================db===========================

    /**
     * 查询信息
     */
    private RechargePropertyConfigEntity loadDbMsg() {
        String sql = "select * from recharge_property_config";
        return GameData.getDB().get(RechargePropertyConfigEntity.class, sql, new Object[]{});
    }

}
