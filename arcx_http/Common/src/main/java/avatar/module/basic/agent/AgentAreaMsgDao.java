package avatar.module.basic.agent;

import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 代理区域信息数据接口
 */
public class AgentAreaMsgDao {
    private static final AgentAreaMsgDao instance = new AgentAreaMsgDao();
    public static final AgentAreaMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询所有信息
     */
    public AgentAreaMsgEntity loadByArea(int areaId) {
        //从缓存获取
        AgentAreaMsgEntity entity = loadCache(areaId);
        if(entity==null){
            //从数据库查询
            entity = loadDbById(areaId);
            if(entity!=null) {
                //设置缓存
                setCache(areaId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private AgentAreaMsgEntity loadCache(int areaId){
        return (AgentAreaMsgEntity) GameData.getCache().get(PrefixMsg.AGENT_AREA_MSG+"_"+areaId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int areaId, AgentAreaMsgEntity entity){
        GameData.getCache().set(PrefixMsg.AGENT_AREA_MSG+"_"+areaId, entity);
    }

    //=========================db===========================

    /**
     * 从数据库查询
     */
    private AgentAreaMsgEntity loadDbById(int id) {
        return GameData.getDB().get(AgentAreaMsgEntity.class, id);
    }

}
