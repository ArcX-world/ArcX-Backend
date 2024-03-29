package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.Web3GameShiftAccountEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**
 * web3 gameShift账号数据接口
 */
public class Web3GameShiftAccountDao {
    private static final Web3GameShiftAccountDao instance = new Web3GameShiftAccountDao();
    public static final Web3GameShiftAccountDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public Web3GameShiftAccountEntity loadByMsg(int userId){
        Web3GameShiftAccountEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
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
    private Web3GameShiftAccountEntity loadCache(int userId){
        return (Web3GameShiftAccountEntity) GameData.getCache().get(UserPrefixMsg.WEB3_GAME_SHIFT_ACCOUNT+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, Web3GameShiftAccountEntity entity){
        GameData.getCache().set(UserPrefixMsg.WEB3_GAME_SHIFT_ACCOUNT+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private Web3GameShiftAccountEntity loadDbByUserId(int userId) {
        String sql = "select * from web3_game_shift_account where user_id=? ";
        return GameData.getDB().get(Web3GameShiftAccountEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加
     */
    public void insert(Web3GameShiftAccountEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //设置缓存
            setCache(entity.getUserId(), entity);
        }
    }

    /**
     * 更新
     */
    public void update(Web3GameShiftAccountEntity entity) {
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUserId(), entity);
        }
    }

}
