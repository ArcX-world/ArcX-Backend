package avatar.module.user.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家在线信息
 */
public class UserOnlineMsgDao {
    private static final UserOnlineMsgDao instance = new UserOnlineMsgDao();
    public static final UserOnlineMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<UserOnlineMsgEntity> loadByUserId(int userId){
        List<UserOnlineMsgEntity> list = loadCache(userId);
        if(list==null){
            //查询数据库
            list = loadDbByUserId(userId);
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<UserOnlineMsgEntity> loadCache(int userId){
        return (List<UserOnlineMsgEntity>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, List<UserOnlineMsgEntity> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<UserOnlineMsgEntity> loadDbByUserId(int userId) {
        String sql = "select * from user_online_msg where user_id=? order by create_time desc";
        List<UserOnlineMsgEntity> list = GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{userId});
        return list==null?new ArrayList<>():list;
    }

}
