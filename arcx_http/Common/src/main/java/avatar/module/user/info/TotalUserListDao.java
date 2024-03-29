package avatar.module.user.info;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.enumMsg.user.UserStatusEnum;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家总信息
 */
public class TotalUserListDao {
    private static final TotalUserListDao instance = new TotalUserListDao();
    public static final TotalUserListDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadAll(){
        //从缓存查找
        List<Integer>  list = loadCache();
        if(list==null){
            //查询数据库
            list = loadDbAll();
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(){
        return (List<Integer> ) GameData.getCache().get(UserPrefixMsg.TOTAL_USER_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.TOTAL_USER_LIST, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(){
        GameData.getCache().removeCache(UserPrefixMsg.TOTAL_USER_LIST);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbAll() {
        String sql = "select id from user_info where status=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{UserStatusEnum.NORMAL.getCode()});
        return StrUtil.retList(list);
    }
}
