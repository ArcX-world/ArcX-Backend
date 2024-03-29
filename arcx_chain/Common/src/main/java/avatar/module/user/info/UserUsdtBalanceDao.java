package avatar.module.user.info;

import avatar.entity.user.info.UserUsdtBalanceEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUsdtUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家USDT余额信息数据接口
 */
public class UserUsdtBalanceDao {
    private static final UserUsdtBalanceDao instance = new UserUsdtBalanceDao();
    public static final UserUsdtBalanceDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserUsdtBalanceEntity loadByMsg(int userId){
        UserUsdtBalanceEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByMsg(userId);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(UserUsdtUtil.initUserUsdtBalanceEntity(userId));
            }
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserUsdtBalanceEntity loadCache(int userId){
        return (UserUsdtBalanceEntity)
                GameData.getCache().get(UserPrefixMsg.USER_USDT_BALANCE+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, UserUsdtBalanceEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_USDT_BALANCE+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserUsdtBalanceEntity loadDbByMsg(int userId) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", userId);//玩家ID
        String sql = SqlUtil.getSql("user_usdt_balance", paramsMap).toString();
        return GameData.getDB().get(UserUsdtBalanceEntity.class, sql, new Object[]{});
    }

    /**
     * 添加数据
     */
    private UserUsdtBalanceEntity insert(UserUsdtBalanceEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //更新缓存
            setCache(entity.getUserId(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public boolean update(UserUsdtBalanceEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
