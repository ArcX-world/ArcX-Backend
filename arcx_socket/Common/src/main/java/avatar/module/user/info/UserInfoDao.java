package avatar.module.user.info;

import avatar.entity.user.info.UserInfoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**
 * 玩家信息数据接口
 */
public class UserInfoDao {
    private static final UserInfoDao instance = new UserInfoDao();
    public static final UserInfoDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserInfoEntity loadByUserId(int userId){
        //从缓存查找
        UserInfoEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
            //更新缓存
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserInfoEntity loadCache(int userId){
        return (UserInfoEntity) GameData.getCache().get(UserPrefixMsg.USER_INFO+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, UserInfoEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_INFO+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserInfoEntity loadDbByUserId(int userId) {
        return GameData.getDB().get(UserInfoEntity.class, userId);
    }

    /**
     * 更新
     */
    public boolean update(UserInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getId(), entity);
        }
        return flag;
    }

}
