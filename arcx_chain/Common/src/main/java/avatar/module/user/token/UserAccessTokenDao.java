package avatar.module.user.token;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家调用凭证
 */
public class UserAccessTokenDao {
    private static final UserAccessTokenDao instance = new UserAccessTokenDao();
    public static final UserAccessTokenDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public int loadByToken(String token){
        //从缓存查找
        int userId = loadCache(token);
        if(userId==0){
            //查询数据库
            userId = loadDbByAccessToken(token);
            if(userId>0){
                //设置缓存
                setCache(token, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private int loadCache(String token){
        if(GameData.getCache().get(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token)==null){
            return 0;
        }else{
            return (Integer) GameData.getCache().get(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token);
        }
    }

    /**
     * 添加缓存
     */
    public void setCache(String token, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_ACCESS_TOKEN+"_"+token, userId);
    }


    //=========================db===========================

    /**
     * 根据调用凭证查询
     */
    private int loadDbByAccessToken(String token) {
        String sql = "select user_id from user_token_msg where access_token=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{token});
        return StrUtil.listNum(list);
    }
}
