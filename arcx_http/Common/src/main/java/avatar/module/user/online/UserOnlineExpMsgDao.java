package avatar.module.user.online;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserOnlineUtil;

/**
 * 玩家在线经验信息数据接口
 */
public class UserOnlineExpMsgDao {
    private static final UserOnlineExpMsgDao instance = new UserOnlineExpMsgDao();
    public static final UserOnlineExpMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserOnlineExpMsg loadByMsg(int userId){
        UserOnlineExpMsg msg = loadCache(userId);
        if(msg==null){
            //查询数据库
            msg = UserOnlineUtil.initUserOnlineExpMsg(userId);
            setCache(userId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserOnlineExpMsg loadCache(int userId){
        return (UserOnlineExpMsg)
                GameData.getCache().get(UserPrefixMsg.USER_OL_EXP+"_"+userId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int userId, UserOnlineExpMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_OL_EXP+"_"+userId, msg);
    }
}
