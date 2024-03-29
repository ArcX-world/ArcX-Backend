package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**
 * 超级玩家配置数据接口
 */
public class SuperPlayerConfigDao {
    private static final SuperPlayerConfigDao instance = new SuperPlayerConfigDao();
    public static final SuperPlayerConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public SuperPlayerConfigEntity loadMsg() {
        //从缓存获取
        SuperPlayerConfigEntity entity = loadCache();
        if(entity==null){
            entity = loadDbMsg();
            if(entity!=null) {
                //设置缓存
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private SuperPlayerConfigEntity loadCache(){
        return (SuperPlayerConfigEntity) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_CONFIG);
    }

    /**
     * 添加缓存
     */
    private void setCache(SuperPlayerConfigEntity entity){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private SuperPlayerConfigEntity loadDbMsg() {
        String sql = "select * from super_player_config";
        return GameData.getDB().get(SuperPlayerConfigEntity.class, sql, new Object[]{});
    }

}
