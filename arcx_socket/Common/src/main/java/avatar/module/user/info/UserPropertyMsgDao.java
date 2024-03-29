package avatar.module.user.info;

import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserPropertyUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家道具信息数据接口
 */
public class UserPropertyMsgDao {
    private static final UserPropertyMsgDao instance = new UserPropertyMsgDao();
    public static final UserPropertyMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserPropertyMsgEntity loadByMsg(int userId, int propertyType){
        UserPropertyMsgEntity entity = loadCache(userId, propertyType);
        if(entity==null){
            //查询数据库
            entity = loadDbByMsg(userId, propertyType);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(UserPropertyUtil.initUserPropertyMsgEntity(userId, propertyType));
            }
            if(entity!=null){
                setCache(userId, propertyType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserPropertyMsgEntity loadCache(int userId, int propertyType){
        return (UserPropertyMsgEntity)
                GameData.getCache().get(UserPrefixMsg.USER_PROPERTY_MSG+"_"+userId+"_"+propertyType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, int propertyType, UserPropertyMsgEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_PROPERTY_MSG+"_"+userId+"_"+propertyType, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserPropertyMsgEntity loadDbByMsg(int userId, int commodityType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);//玩家ID
        paramsMap.put("property_type", commodityType);//商品类型
        String sql = SqlUtil.getSql("user_property_msg", paramsMap).toString();
        return GameData.getDB().get(UserPropertyMsgEntity.class, sql, new Object[]{});
    }

    /**
     * 添加数据
     */
    private UserPropertyMsgEntity insert(UserPropertyMsgEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //更新缓存
            setCache(entity.getUserId(), entity.getPropertyType(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public boolean update(UserPropertyMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getUserId(), entity.getPropertyType(), entity);
        }
        return flag;
    }

}
