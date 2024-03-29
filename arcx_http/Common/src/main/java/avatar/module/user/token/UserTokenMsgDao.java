package avatar.module.user.token;

import avatar.entity.user.token.UserTokenMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserTokenUtil;

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
            if(entity==null){
                //添加数据
                entity = insert(userId);

            }
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

    /**
     * 删除缓存
     */
    private void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_TOKEN_MSG+"_"+userId);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserTokenMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_token_msg where user_id=?";
        return GameData.getDB().get(UserTokenMsgEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加数据
     */
    private UserTokenMsgEntity insert(int userId) {
        UserTokenMsgEntity entity = new UserTokenMsgEntity();
        entity.setUserId(userId);//玩家ID
        //调用凭证
        String accessToken = UserTokenUtil.initUserAccessToken(userId);
        entity.setAccessToken(accessToken);
        entity.setAccessOutTime(UserTokenUtil.userAccessTokenOutTime());//调用凭证过期时间
        //刷新凭证
        String refreshToken = UserTokenUtil.initUserRefreshToken(userId);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshOutTime(UserTokenUtil.userRefreshTokenOutTime());//刷新凭证过期时间
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //设置缓存
            setCache(userId, entity);
            //设置调用凭证缓存
            UserAccessTokenDao.getInstance().setCache(accessToken, userId);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public UserTokenMsgEntity update(int userId){
        UserTokenMsgEntity entity = loadByUserId(userId);
        String oriAccessToken = entity.getAccessToken();//初始的token
        //调用凭证
        String accessToken = UserTokenUtil.initUserAccessToken(userId);
        entity.setAccessToken(accessToken);
        entity.setAccessOutTime(UserTokenUtil.userAccessTokenOutTime());//调用凭证过期时间
        //刷新凭证
        String refreshToken = UserTokenUtil.initUserRefreshToken(userId);
        entity.setRefreshToken(refreshToken);
        entity.setRefreshOutTime(UserTokenUtil.userRefreshTokenOutTime());//刷新凭证过期时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(userId, entity);
            //调用凭证
            UserAccessTokenDao.getInstance().removeCache(oriAccessToken);//删除旧的调用凭证缓存
            UserAccessTokenDao.getInstance().setCache(accessToken, userId);//添加新的调用凭证缓存
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 删除玩家token
     */
    public void deleteTokenMsg(int userId){
        UserTokenMsgEntity entity = loadByUserId(userId);
        if(entity!=null){
            //删除信息
            GameData.getDB().delete(entity);
            //删除token信息
            removeCache(userId);
            //删除缓存信息
            UserAccessTokenDao.getInstance().removeCache(entity.getAccessToken());
        }
    }
}
