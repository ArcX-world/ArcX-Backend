package avatar.module.user.online;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 玩家在线列表信息
 */
public class UserOnlineListDao {
    private static final UserOnlineListDao instance = new UserOnlineListDao();
    public static final UserOnlineListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<Integer> loadAll(){
        List<Integer> list = loadCache();
        if(list==null){
            //查询数据库
            list = loadDbAll();
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(){
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_LIST, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(){
        GameData.getCache().removeCache(UserPrefixMsg.USER_ONLINE_LIST);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<Integer> loadDbAll() {
        String sql = SqlUtil.appointListSql("user_online_msg", "user_id",
                new HashMap<>(), Collections.singletonList("create_time")).toString();
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
