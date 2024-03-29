package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.SuperPlayerUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserUtil;

/**
 * 超级玩家玩家信息数据接口
 */
public class SuperPlayerUserDao {
    private static final SuperPlayerUserDao instance = new SuperPlayerUserDao();
    public static final SuperPlayerUserDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public SuperPlayerUserMsgEntity loadMsg(int userId) {
        //从缓存获取
        SuperPlayerUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            entity = loadDbMsg(userId);
            if(entity==null && UserUtil.existUser(userId)){
                entity = insert(SuperPlayerUtil.initSuperPlayerUserMsgEntity(userId));
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
    private SuperPlayerUserMsgEntity loadCache(int userId){
        return (SuperPlayerUserMsgEntity) GameData.getCache().get(RechargePrefixMsg.SUPER_PLAYER_USER_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, SuperPlayerUserMsgEntity entity){
        GameData.getCache().set(RechargePrefixMsg.SUPER_PLAYER_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private SuperPlayerUserMsgEntity loadDbMsg(int userId) {
        String sql = "select * from super_player_user_msg where user_id=?";
        return GameData.getDB().get(SuperPlayerUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加
     */
    private SuperPlayerUserMsgEntity insert(SuperPlayerUserMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            //设置缓存
            setCache(entity.getUserId(), entity);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 编辑
     */
    public boolean update(SuperPlayerUserMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUserId(), entity);
            //重置超级玩家列表缓存
            SuperPlayerUserListDao.getInstance().loadMsg();
        }
        return flag;
    }

}
