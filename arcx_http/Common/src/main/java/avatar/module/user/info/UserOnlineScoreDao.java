package avatar.module.user.info;

import avatar.data.user.balance.UserOnlineScoreMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.user.UserBalanceUtil;

/**
 * 玩家在线分数信息数据接口
 */
public class UserOnlineScoreDao {
    private static final UserOnlineScoreDao instance = new UserOnlineScoreDao();
    public static final UserOnlineScoreDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserOnlineScoreMsg loadByMsg(int userId, int commodityType){
        UserOnlineScoreMsg msg = loadCache(userId, commodityType);
        if(msg==null){
            //查询数据库
            msg = UserBalanceUtil.initUserOnlineScoreMsg(userId, commodityType);
            setCache(userId, commodityType, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserOnlineScoreMsg loadCache(int userId, int commodityType){
        return (UserOnlineScoreMsg)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_SCORE+"_"+userId+"_"+commodityType);
    }

    /**
     * 添加缓存
     */
    public void setCache(int userId, int commodityType, UserOnlineScoreMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_SCORE+"_"+userId+"_"+commodityType, msg);
    }
}
