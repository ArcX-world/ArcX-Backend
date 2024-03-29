package avatar.module.basic.agent;

import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 代理国家区域信息数据接口
 */
public class AgentNationAreaMsgDao {
    private static final AgentNationAreaMsgDao instance = new AgentNationAreaMsgDao();
    public static final AgentNationAreaMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询所有信息
     */
    public int loadByNation(String nation) {
        //从缓存获取
        int areaId = loadCache(nation);
        if(areaId==-1){
            //从数据库查询
            areaId = loadDbByNation(nation);
            //设置缓存
            setCache(nation, areaId);
        }
        return areaId;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private int loadCache(String nation){
        Object obj = GameData.getCache().get(PrefixMsg.AGENT_NATION_AREA_MSG+"_"+nation);
        return obj==null?-1:((int) obj);
    }

    /**
     * 添加缓存
     */
    private void setCache(String nation, int areaId){
        GameData.getCache().set(PrefixMsg.AGENT_NATION_AREA_MSG+"_"+nation, areaId);
    }

    //=========================db===========================

    /**
     * 从数据库查询
     */
    private int loadDbByNation(String nation) {
        String sql = "select area_id from agent_area_nation_msg where nation_name=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{nation});
        return StrUtil.listSize(list)>0?list.get(0):0;
    }

}
