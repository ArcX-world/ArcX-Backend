package avatar.module.basic.agent;

import avatar.entity.basic.agent.AgentDefaultMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;

/**
 * 代理默认信息数据接口
 */
public class AgentDefaultMsgDao {
    private static final AgentDefaultMsgDao instance = new AgentDefaultMsgDao();
    public static final AgentDefaultMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public AgentDefaultMsgEntity loadMsg(){
        AgentDefaultMsgEntity entity = loadCache();
        if(entity==null){
            //查询数据库
            entity = loadDbMsg();
            setCache(entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private AgentDefaultMsgEntity loadCache(){
        return (AgentDefaultMsgEntity)
                GameData.getCache().get(PrefixMsg.AGENT_DEFAULT_MSG);
    }

    /**
     * 添加缓存
     */
    private void setCache(AgentDefaultMsgEntity entity){
        GameData.getCache().set(PrefixMsg.AGENT_DEFAULT_MSG, entity);
    }

    //=========================db===========================

    /**
     * 根据ID查询
     */
    private AgentDefaultMsgEntity loadDbMsg() {
        String sql = "select * from agent_default_msg";
        return GameData.getDB().get(AgentDefaultMsgEntity.class, sql, new Object[]{});
    }

}
