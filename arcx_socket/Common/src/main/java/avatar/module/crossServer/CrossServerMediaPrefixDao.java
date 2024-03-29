package avatar.module.crossServer;

import avatar.entity.crossServer.CrossServerMediaPrefixEntity;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;

/**
 * 跨服多媒体信息
 */
public class CrossServerMediaPrefixDao {
    private static final CrossServerMediaPrefixDao instance = new CrossServerMediaPrefixDao();
    public static final CrossServerMediaPrefixDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public CrossServerMediaPrefixEntity loadByServerType(int serverType){
        //从缓存查找
        CrossServerMediaPrefixEntity entity = loadCache(serverType);
        if(entity==null){
            //查询数据库
            entity = loadDbByServerType(serverType);
            if(entity!=null) {
                //更新缓存
                setCache(serverType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private CrossServerMediaPrefixEntity loadCache(int serverType){
        return (CrossServerMediaPrefixEntity) GameData.getCache().get(CrossServerPrefixMsg.CROSS_SERVER_MEDIA_PREFIX+"_"+serverType);
    }

    /**
     * 添加缓存
     */
    public void setCache(int serverType, CrossServerMediaPrefixEntity msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_MEDIA_PREFIX+"_"+serverType, msg);
    }

    //=========================db===========================

    /**
     * 根据服务端类型查询
     */
    private CrossServerMediaPrefixEntity loadDbByServerType(int serverType) {
        String sql = "select * from cross_server_media_prefix where server_type=?";
        return GameData.getDB().get(CrossServerMediaPrefixEntity.class, sql, new Object[]{serverType});
    }
}
