package avatar.module.product.gaming;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家加入设备数据接口
 */
public class UserJoinProductDao {
    private static final UserJoinProductDao instance = new UserJoinProductDao();
    public static final UserJoinProductDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadByMsg(int userId) {
        List<Integer> list = loadCache(userId);
        if(list==null){
            list = new ArrayList<>();
            //设置缓存
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(int userId){
        return (List<Integer>) GameData.getCache().get(
                ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId);
    }

    /**
     * 设置缓存
     */
    public void setCache(int userId, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(ProductPrefixMsg.USER_SESSION_JOIN_PRODUCT_MSG+"_"+userId);
    }

}
