package avatar.module.crossServer;

import avatar.global.prefixMsg.CrossServerPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 跨服服务端列表数据接口
 */
public class CrossServerListDao {
    private static final CrossServerListDao instance = new CrossServerListDao();
    public static final CrossServerListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<Integer> loadAll(){
        //从缓存查找
        List<Integer> list = loadCache();
        if(list==null){
            //查询信息
            list = loadDbAll();
            //更新缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(){
        return (List<Integer>) GameData.getCache().get(CrossServerPrefixMsg.CROSS_SERVER_LIST);
    }

    /**
     * 添加缓存
     */
    public void setCache(List<Integer> list){
        GameData.getCache().set(CrossServerPrefixMsg.CROSS_SERVER_LIST, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbAll() {
        String sql = "select server_type from cross_server_domain";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
