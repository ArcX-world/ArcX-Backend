package avatar.module.basic.systemMsg;

import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 道具信息数据接口
 */
public class PropertyMsgDao {
    private static final PropertyMsgDao instance = new PropertyMsgDao();
    public static final PropertyMsgDao getInstance() {
        return instance;
    }

    /**
     * 查询信息
     */
    public PropertyMsgEntity loadMsg(int propertyType) {
        //从缓存获取
        PropertyMsgEntity entity = loadCache(propertyType);
        if (entity==null) {
            //查询数据库
            entity = loadDbMsg(propertyType);
            //设置缓存
            setCache(propertyType, entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private PropertyMsgEntity loadCache(int propertyType) {
        return (PropertyMsgEntity) GameData.getCache().get(PrefixMsg.PROPERTY_MSG+"_"+propertyType);
    }

    /**
     * 设置缓存
     *
     */
    private void setCache(int propertyType, PropertyMsgEntity entity) {
        GameData.getCache().set(PrefixMsg.PROPERTY_MSG+"_"+propertyType, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private PropertyMsgEntity loadDbMsg(int propertyType) {
        String sql = "select * from property_msg where property_type=?";
        return GameData.getDB().get(PropertyMsgEntity.class, sql, new Object[]{propertyType});
    }

}
