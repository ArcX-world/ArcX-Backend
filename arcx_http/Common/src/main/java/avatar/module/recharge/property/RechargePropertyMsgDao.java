package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyMsgEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**
 * 充值道具信息数据接口
 */
public class RechargePropertyMsgDao {
    private static final RechargePropertyMsgDao instance = new RechargePropertyMsgDao();
    public static final RechargePropertyMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public RechargePropertyMsgEntity loadMsg(int id) {
        //从缓存获取
        RechargePropertyMsgEntity entity = loadCache(id);
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
    private RechargePropertyMsgEntity loadCache(int id){
        return (RechargePropertyMsgEntity) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_MSG+"_"+id);
    }

    /**
     * 添加缓存
     */
    private void setCache(int id, RechargePropertyMsgEntity entity){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_MSG+"_"+id, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private RechargePropertyMsgEntity loadDbById(int id) {
        return GameData.getDB().get(RechargePropertyMsgEntity.class, id);
    }

}
