package avatar.module.user.online;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备玩家在线列表信息
 */
public class UserProductOnlineListDao {
    private static final UserProductOnlineListDao instance = new UserProductOnlineListDao();
    public static final UserProductOnlineListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<Integer> loadByProductId(int productId){
        List<Integer> list = loadCache(productId);
        if(list==null){
            //查询数据库
            list = loadDbByProductId(productId);
            setCache(productId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(int productId){
        return (List<Integer>)
                GameData.getCache().get(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int productId, List<Integer> list){
        GameData.getCache().set(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(int productId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_PRODUCT_ONLINE_LIST+"_"+productId);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbByProductId(int productId) {
        String sql = "select user_id from user_online_msg where product_id=? order by create_time";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{productId});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
