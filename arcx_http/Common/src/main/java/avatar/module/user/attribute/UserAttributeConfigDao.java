package avatar.module.user.attribute;

import avatar.entity.user.attribute.UserAttributeConfigEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**
 * 玩家属性配置数据接口
 */
public class UserAttributeConfigDao {
    private static final UserAttributeConfigDao instance = new UserAttributeConfigDao();
    public static final UserAttributeConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserAttributeConfigEntity loadMsg(int lv){
        UserAttributeConfigEntity entity = loadCache(lv);
        if(entity==null){
            entity = loadDbByLv(lv);
            if(entity!=null) {
                setCache(lv, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserAttributeConfigEntity loadCache(int lv){
        return (UserAttributeConfigEntity)
                GameData.getCache().get(UserPrefixMsg.USER_ATTRIBUTE_CONFIG+"_"+lv);
    }

    /**
     * 添加缓存
     */
    private void setCache(int lv, UserAttributeConfigEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_ATTRIBUTE_CONFIG+"_"+lv, entity);
    }

    //=========================db===========================

    /**
     * 根据等级查询
     */
    private UserAttributeConfigEntity loadDbByLv(int lv) {
        String sql = "select * from user_attribute_config where lv=?";
        return GameData.getDB().get(UserAttributeConfigEntity.class, sql, new Object[]{lv});
    }

}
