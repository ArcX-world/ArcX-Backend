package avatar.module.user.info;

import avatar.entity.user.info.UserGrandPrizeMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

/**
 * 玩家设备大奖信息数据接口
 */
public class UserGrandPrizeMsgDao {
    private static final UserGrandPrizeMsgDao instance = new UserGrandPrizeMsgDao();
    public static final UserGrandPrizeMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询设备大奖信息
     */
    public UserGrandPrizeMsgEntity loadByUserId(int userId){
        UserGrandPrizeMsgEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
            if(entity==null){
                //添加数据
                entity = insert(userId);
            }
            if(entity!=null){
                //设置缓存
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserGrandPrizeMsgEntity loadCache(int userId){
        return (UserGrandPrizeMsgEntity)
                GameData.getCache().get(UserPrefixMsg.USER_GRAND_PRIZE_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int userId, UserGrandPrizeMsgEntity entity){
        //保存缓存信息
        GameData.getCache().set(UserPrefixMsg.USER_GRAND_PRIZE_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询信息
     */
    private UserGrandPrizeMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from user_grand_prize_msg where user_id=?";
        return GameData.getDB().get(UserGrandPrizeMsgEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加数据
     */
    private UserGrandPrizeMsgEntity insert(int userId) {
        UserGrandPrizeMsgEntity entity = UserUtil.initUserGrandPrizeMsgEntity(userId);
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public boolean update(UserGrandPrizeMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
