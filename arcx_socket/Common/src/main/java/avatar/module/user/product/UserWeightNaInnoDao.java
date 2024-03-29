package avatar.module.user.product;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 玩家权重NA数据接口
 */
public class UserWeightNaInnoDao {
    private static final UserWeightNaInnoDao instance = new UserWeightNaInnoDao();
    public static final UserWeightNaInnoDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<UserWeightNaInnoEntity> loadByUserId(int userId){
        //从缓存获取
        List<UserWeightNaInnoEntity> list = loadCache(userId);
        if(list==null){
            //查询数据库
            list = loadDbByUserId(userId);
            if(list==null){
                list = new ArrayList<>();
            }
            //设置缓存
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存信息
     */
    private List<UserWeightNaInnoEntity> loadCache(int userId) {
        return (List<UserWeightNaInnoEntity>) GameData.getCache().get(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId);
    }

    /**
     * 设置缓存
     */
    private void setCache(int userId, List<UserWeightNaInnoEntity> entity) {
        GameData.getCache().set(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId, entity);
    }

    /**
     * 删除缓存
     */
    public void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_WEIGHT_NA_INNO+"_"+userId);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private List<UserWeightNaInnoEntity> loadDbByUserId(int userId) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);//玩家ID
        String sql = SqlUtil.loadList("user_weight_na_inno", paramsMap,
                new ArrayList<>()).toString();
        return GameData.getDB().list(UserWeightNaInnoEntity.class, sql, new Object[]{});
    }
}
