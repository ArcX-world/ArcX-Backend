package avatar.module.basic.systemMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.PrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 道具列表数据接口
 */
public class PropertyListDao {
    private static final PropertyListDao instance = new PropertyListDao();
    public static final PropertyListDao getInstance() {
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadMsg() {
        //从缓存获取
        List<Integer> list = loadCache();
        if (list==null) {
            //查询数据库
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
    private List<Integer> loadCache() {
        return (List<Integer>) GameData.getCache().get(PrefixMsg.PROPERTY_LIST);
    }

    /**
     * 设置缓存
     *
     */
    private void setCache(List<Integer> list) {
        GameData.getCache().set(PrefixMsg.PROPERTY_LIST, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select property_type from property_msg where active_flag=? order by sequence,property_type";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{YesOrNoEnum.YES.getCode()});
        return StrUtil.retList(list);
    }
}
