package avatar.module.crossServer;

import avatar.entity.crossServer.CrossServerDomainEntity;
import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;

/**
 * 跨服域名信息数据接口
 */
public class CrossServerDomainDao {
    private static final CrossServerDomainDao instance = new CrossServerDomainDao();
    public static final CrossServerDomainDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public CrossServerDomainEntity loadByMsg(int serverSideType){
        //从缓存查找
        CrossServerDomainEntity entity = loadCache(serverSideType);
        if(entity==null){
            //查询信息
            entity = loadDbByMsg(serverSideType);
            if(entity!=null) {
                //更新缓存
                setCache(serverSideType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private CrossServerDomainEntity loadCache(int serverSideType){
        return (CrossServerDomainEntity) GameData.getCache().get(
                CrossServerPrefixMsg.CROSS_SERVER_DOMAIN+"_"+serverSideType);
    }

    /**
     * 添加缓存
     */
    public void setCache(int serverSideType, CrossServerDomainEntity msg){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_DOMAIN+"_"+serverSideType, msg);
    }

    //=========================db===========================

    /**
     * 根据信息查询
     */
    private CrossServerDomainEntity loadDbByMsg(int serverSideType) {
        String sql = "select * from cross_server_domain where server_type=?";
        return GameData.getDB().get(CrossServerDomainEntity.class, sql, new Object[]{serverSideType});
    }
}
