package avatar.module.user.product;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserWeightUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家精确权重NA数据接口
 */
public class UserPreciseWeightNaInnoDao {
    private static final UserPreciseWeightNaInnoDao instance = new UserPreciseWeightNaInnoDao();
    public static final UserPreciseWeightNaInnoDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public UserWeightNaInnoEntity loadMsg(int userId, int secondType){
        //从缓存获取
        UserWeightNaInnoEntity entity = loadCache(userId, secondType);
        if(entity==null){
            entity = loadDbMsg(userId, secondType);
            if(entity==null){
                entity = insert(UserWeightUtil.initUserWeightNaInnoEntity(userId, secondType));
            }
            //设置缓存
            if(entity!=null) {
                setCache(userId, secondType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存信息
     */
    private UserWeightNaInnoEntity loadCache(int userId, int secondType) {
        return (UserWeightNaInnoEntity) GameData.getCache().get(
                UserPrefixMsg.USER_PRECISE_WEIGHT_NA_INNO+"_"+userId+"_"+secondType);
    }

    /**
     * 设置缓存
     */
    public void setCache(int userId,  int secondType, UserWeightNaInnoEntity entity) {
        GameData.getCache().set(UserPrefixMsg.USER_PRECISE_WEIGHT_NA_INNO+"_"+userId+"_"+secondType, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserWeightNaInnoEntity loadDbMsg(int userId, int secondType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);//玩家ID
        paramsMap.put("second_type", secondType);//设备二级分类
        String sql = SqlUtil.getSql("user_weight_na_inno", paramsMap).toString();
        return GameData.getDB().get(UserWeightNaInnoEntity.class, sql, new Object[]{});
    }

    /**
     * 添加
     */
    private UserWeightNaInnoEntity insert(UserWeightNaInnoEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //重置玩家NA缓存
            UserWeightNaInnoDao.getInstance().removeCache(entity.getUserId());
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public void update(UserWeightNaInnoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUserId(), entity.getSecondType(), entity);
            //重置玩家NA缓存
            UserWeightNaInnoDao.getInstance().removeCache(entity.getUserId());
        }
    }
}
