package avatar.module.user.info;

import avatar.entity.user.info.UserPlatformBalanceEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.basic.general.CommodityUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserBalanceUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家平台数据接口
 */
public class UserPlatformBalanceDao {
    private static final UserPlatformBalanceDao instance = new UserPlatformBalanceDao();
    public static final UserPlatformBalanceDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserPlatformBalanceEntity loadByMsg(int userId, int commodityType){
        UserPlatformBalanceEntity entity = loadCache(userId, commodityType);
        if(entity==null){
            //查询数据库
            entity = loadDbByMsg(userId, commodityType);
            if(entity==null && UserUtil.existUser(userId) && CommodityUtil.normalFlag(commodityType)){
                entity = insert(UserBalanceUtil.initUserPlatformBalanceEntity(userId, commodityType));
            }
            if(entity!=null){
                setCache(userId, commodityType, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserPlatformBalanceEntity loadCache(int userId, int commodityType){
        return (UserPlatformBalanceEntity)
                GameData.getCache().get(UserPrefixMsg.USER_PLATFORM_BALANCE+"_"+userId+"_"+commodityType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, int commodityType, UserPlatformBalanceEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_PLATFORM_BALANCE+"_"+userId+"_"+commodityType, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserPlatformBalanceEntity loadDbByMsg(int userId, int commodityType) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);//玩家ID
        paramsMap.put("commodity_type", commodityType);//商品类型
        String sql = SqlUtil.getSql("user_platform_balance", paramsMap).toString();
        return GameData.getDB().get(UserPlatformBalanceEntity.class, sql, new Object[]{});
    }

    /**
     * 添加数据
     */
    private UserPlatformBalanceEntity insert(UserPlatformBalanceEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //更新缓存
            setCache(entity.getUserId(), entity.getCommodityType(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public boolean update(UserPlatformBalanceEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getUserId(), entity.getCommodityType(), entity);
        }
        return flag;
    }
}
