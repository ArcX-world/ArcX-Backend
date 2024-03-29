package avatar.module.activity.dragonTrain.user;

import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.activity.DragonTrainUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 龙珠玛丽机玩家信息数据接口
 */
public class DragonTrainUserMsgDao {
    private static final DragonTrainUserMsgDao instance = new DragonTrainUserMsgDao();
    public static final DragonTrainUserMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public DragonTrainUserMsgEntity loadByUserId(int userId) {
        //从缓存获取
        DragonTrainUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            //从数据库查询
            entity = loadDbByUserId(userId);
            if(entity==null){
                //添加信息
                entity = insert(DragonTrainUtil.initDragonTrainUserMsgEntity(userId));
            }
            if(entity!=null) {
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
    private DragonTrainUserMsgEntity loadCache(int userId){
        return (DragonTrainUserMsgEntity) GameData.getCache().get(ActivityPrefixMsg.DRAGON_TRAIN_USER_MSG+"_"+userId);
    }

    /**
     * 设置缓存
     */
    private void setCache(int userId, DragonTrainUserMsgEntity entity){
        GameData.getCache().set(ActivityPrefixMsg.DRAGON_TRAIN_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 查询配置信息
     */
    private DragonTrainUserMsgEntity loadDbByUserId(int userId) {
        Map<String, Object> equalsParams = new HashMap<>();
        equalsParams.put("user_id", userId);
        String sql = SqlUtil.getSql("dragon_train_user_msg", equalsParams).toString();
        return GameData.getDB().get(DragonTrainUserMsgEntity.class, sql, new Object[]{});
    }

    /**
     * 添加
     */
    private DragonTrainUserMsgEntity insert(DragonTrainUserMsgEntity entity) {
        int id  = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            return entity;
        }
        return null;
    }

    /**
     * 更新
     */
    public boolean update(DragonTrainUserMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
