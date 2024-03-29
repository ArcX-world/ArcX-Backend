package avatar.module.recharge.superPlayer;

import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 超级玩家列表数据接口
 */
public class SuperPlayerUserListDao {
    private static final SuperPlayerUserListDao instance = new SuperPlayerUserListDao();
    public static final SuperPlayerUserListDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadMsg() {
        //从缓存获取
        List<Integer> list = loadCache();
        if(list==null){
            list = loadDbMsg();
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_USER_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_USER_LIST, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(){
        GameData.getCache().removeCache(RechargePrefixMsg.SUPER_PLAYER_USER_LIST);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select user_id from super_player_user_msg where effect_time>='"+ TimeUtil.getNowTimeStr() +"'";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
