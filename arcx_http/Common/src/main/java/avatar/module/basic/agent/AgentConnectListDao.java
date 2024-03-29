package avatar.module.basic.agent;

import avatar.data.basic.agent.AgentConnectMsg;
import avatar.entity.basic.agent.AgentAreaMsgEntity;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.AgentMsgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理连接列表数据接口
 */
public class AgentConnectListDao {
    private static final AgentConnectListDao instance = new AgentConnectListDao();
    public static final AgentConnectListDao getInstance(){
        return instance;
    }

    /**
     * 查询所有连接类型信息
     */
    public List<AgentConnectMsg> loadMsg() {
        //从缓存获取
        List<AgentConnectMsg> list = loadCache();
        if(list==null){
            //从数据库查询
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
    private List<AgentConnectMsg> loadCache(){
        return (List<AgentConnectMsg>) GameData.getCache().get(PrefixMsg.AGENT_CONNECT_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<AgentConnectMsg> list){
        GameData.getCache().set(PrefixMsg.AGENT_CONNECT_LIST, list);
    }


    //=========================db===========================

    /**
     * 查询信息
     */
    private List<AgentConnectMsg> loadDbAll() {
        List<AgentConnectMsg> retList = new ArrayList<>();
        String sql = "select * from agent_area_msg";
        List<AgentAreaMsgEntity> list = GameData.getDB().list(AgentAreaMsgEntity.class, sql, new Object[]{});
        //填充信息
        AgentMsgUtil.initAgentConnectMsg(list, retList);
        return retList;
    }
}
