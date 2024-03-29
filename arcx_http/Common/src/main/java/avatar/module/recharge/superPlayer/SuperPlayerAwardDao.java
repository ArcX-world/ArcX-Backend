package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerAwardEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

import java.util.List;

/**
 * 超级玩家奖励数据接口
 */
public class SuperPlayerAwardDao {
    private static final SuperPlayerAwardDao instance = new SuperPlayerAwardDao();
    public static final SuperPlayerAwardDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<SuperPlayerAwardEntity> loadMsg() {
        //从缓存获取
        List<SuperPlayerAwardEntity> list = loadCache();
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
    private List<SuperPlayerAwardEntity> loadCache(){
        return (List<SuperPlayerAwardEntity>) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_AWARD);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<SuperPlayerAwardEntity> list){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_AWARD, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<SuperPlayerAwardEntity> loadDbMsg() {
        String sql = "select * from super_player_award order by sequence";
        return GameData.getDB().list(SuperPlayerAwardEntity.class, sql, new Object[]{});
    }

}
