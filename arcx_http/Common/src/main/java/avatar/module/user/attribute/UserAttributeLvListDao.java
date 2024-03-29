package avatar.module.user.attribute;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家属性等级列表数据接口
 */
public class UserAttributeLvListDao {
    private static final UserAttributeLvListDao instance = new UserAttributeLvListDao();
    public static final UserAttributeLvListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<Integer> loadMsg(){
        List<Integer> list = loadCache();
        if(list==null){
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
    private List<Integer> loadCache(){
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_ATTRIBUTE_LV_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_ATTRIBUTE_LV_LIST, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select lv from user_attribute_config order by lv";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
