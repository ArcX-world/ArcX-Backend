package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.RechargeGoldInfoEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**
 * 充值金币信息数据接口
 */
public class RechargeGoldInfoDao {
    private static final RechargeGoldInfoDao instance = new RechargeGoldInfoDao();
    public static final RechargeGoldInfoDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public RechargeGoldInfoEntity loadById(int id) {
        //从缓存获取
        RechargeGoldInfoEntity entity = loadCache(id);
        if(entity==null){
            entity = loadDbById(id);
            if(entity!=null) {
                //设置缓存
                setCache(id, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private RechargeGoldInfoEntity loadCache(int id){
        return (RechargeGoldInfoEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_GOLD_INFO+"_"+id);
    }

    /**
     * 添加缓存
     */
    private void setCache(int id, RechargeGoldInfoEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_GOLD_INFO+"_"+id, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private RechargeGoldInfoEntity loadDbById(int id) {
        return GameData.getDB().get(RechargeGoldInfoEntity.class, id);
    }

}
