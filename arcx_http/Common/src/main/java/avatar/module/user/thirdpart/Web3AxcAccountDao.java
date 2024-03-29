package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

/**
 * web3 axc代币账号数据接口
 */
public class Web3AxcAccountDao {
    private static final Web3AxcAccountDao instance = new Web3AxcAccountDao();
    public static final Web3AxcAccountDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public Web3AxcAccountEntity loadByMsg(int userId){
        Web3AxcAccountEntity entity = loadCache(userId);
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
    private Web3AxcAccountEntity loadCache(int userId){
        return (Web3AxcAccountEntity) GameData.getCache().get(UserPrefixMsg.WEB3_AXC_ACCOUNT+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, Web3AxcAccountEntity entity){
        GameData.getCache().set(UserPrefixMsg.WEB3_AXC_ACCOUNT+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private Web3AxcAccountEntity loadDbByUserId(int userId) {
        String sql = "select * from web3_axc_account where user_id=? ";
        return GameData.getDB().get(Web3AxcAccountEntity.class, sql, new Object[]{userId});
    }

    /**
     * 添加
     */
    public void insert(Web3AxcAccountEntity entity) {
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);
            //设置缓存
            setCache(entity.getUserId(), entity);
        }
    }
}
