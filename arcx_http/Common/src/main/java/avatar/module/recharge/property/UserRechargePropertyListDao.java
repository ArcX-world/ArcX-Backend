package avatar.module.recharge.property;

import avatar.data.recharge.UserRechargePropertyMsg;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.RechargePropertyUtil;

/**
 * 玩家充值道具列表数据接口
 */
public class UserRechargePropertyListDao {
    private static final UserRechargePropertyListDao instance = new UserRechargePropertyListDao();
    public static final UserRechargePropertyListDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public UserRechargePropertyMsg loadMsg(int userId) {
        //从缓存获取
        UserRechargePropertyMsg msg = loadCache(userId);
        if(msg==null){
            msg = RechargePropertyUtil.initUserRechargePropertyMsg(userId);
            //设置缓存
            setCache(userId, msg);
        }
        if(msg.getPropertyList().size()>0){
            //处理返回信息
            RechargePropertyUtil.dealUserRetPropertyMsg(msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserRechargePropertyMsg loadCache(int userId){
        return (UserRechargePropertyMsg) GameData.getCache().get(RechargePrefixMsg.USER_RECHARGE_PROPERTY_LIST+"_"+userId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int userId, UserRechargePropertyMsg msg){
        GameData.getCache().set(RechargePrefixMsg.USER_RECHARGE_PROPERTY_LIST+"_"+userId, msg);
    }
}
