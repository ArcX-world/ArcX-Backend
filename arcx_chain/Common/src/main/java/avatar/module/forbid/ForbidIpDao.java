package avatar.module.forbid;

import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 封禁IP数据接口
 */
public class ForbidIpDao {
    private static final ForbidIpDao instance = new ForbidIpDao();
    public static final ForbidIpDao getInstance() {
        return instance;
    }

    /**
     * 查询信息
     */
    public List<String> loadAll() {
        //从缓存获取
        List<String> list = loadCache();
        if (list == null || list.size()==0) {
            //查询数据库
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
    private List<String> loadCache() {
        return (List<String>)
                GameData.getCache().get(PrefixMsg.FORBID_IP);
    }

    /**
     * 设置缓存
     *
     */
    private void setCache(List<String> list) {
        GameData.getCache().set(PrefixMsg.FORBID_IP, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<String> loadDbAll() {
        String sql = SqlUtil.appointListSql("forbid_ip", "ip", new HashMap<>(),
                Collections.singletonList("create_time")).toString();
        List<String> list = GameData.getDB().listString(sql, new Object[]{});
        if(StrUtil.strListSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
