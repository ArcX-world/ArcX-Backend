package avatar.module.user.token;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**
 * 玩家凭证信息
 */
public class UserTokenMsgDao {
    private static final UserTokenMsgDao instance = new UserTokenMsgDao();
    public static final UserTokenMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserTokenMsgEntity loadByUserId(int userId){
        //从缓存查找
        UserTokenMsgEntity entity = loadCache(userId);
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
    private UserTokenMsgEntity loadCache(int userId){
        return (UserTokenMsgEntity) GameData.getCache().get(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, UserTokenMsgEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserTokenMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_token_msg where user_id=?";
        return GameData.getDB().get(UserTokenMsgEntity.class, sql, new Object[]{userId});
    }

}
