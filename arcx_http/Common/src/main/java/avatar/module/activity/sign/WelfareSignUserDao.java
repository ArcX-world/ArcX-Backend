package avatar.module.activity.sign;

import avatar.entity.activity.sign.user.WelfareSignUserMsgEntity;
import avatar.global.prefixMsg.ActivityPrefixMsg;
import avatar.util.GameData;
import avatar.util.activity.WelfareUtil;

/**
 * 玩家签到数据接口
 */
public class WelfareSignUserDao {
    private static final WelfareSignUserDao instance = new WelfareSignUserDao();
    public static final WelfareSignUserDao getInstance(){
        return instance;
    }

    /**
     * 根据玩家ID查询福利签到玩家信息
     */
    public WelfareSignUserMsgEntity loadByUserId(int userId){
        //从缓存获取
        WelfareSignUserMsgEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
            if(entity==null){
                //添加数据
                entity = insert(userId);
            }
            //设置缓存
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        if(entity!=null) {
            //处理玩家签到信息
            WelfareUtil.dealUserSignMsg(entity);
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存信息
     */
    private WelfareSignUserMsgEntity loadCache(int userId) {
        return (WelfareSignUserMsgEntity) GameData.getCache().get(ActivityPrefixMsg.WELFARE_SIGN_USER_MSG+"_"+userId);
    }

    /**
     * 设置缓存
     */
    private void setCache(int userId, WelfareSignUserMsgEntity entity) {
        GameData.getCache().set(ActivityPrefixMsg.WELFARE_SIGN_USER_MSG+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询福利签到玩家信息
     */
    private WelfareSignUserMsgEntity loadDbByUserId(int userId) {
        String sql = "select * from welfare_sign_user_msg where user_id=?";
        return GameData.getDB().get(WelfareSignUserMsgEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加
     */
    public WelfareSignUserMsgEntity insert(int userId) {
        //填充实体信息
        WelfareSignUserMsgEntity entity = WelfareUtil.initWelfareSignUserMsgEntity(userId);
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            //设置缓存
            setCache(userId, entity);
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新签到信息
     */
    public boolean update(WelfareSignUserMsgEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        //更新缓存
        if(flag){
            setCache(entity.getUserId(), entity);
        }
        return flag;
    }
}
