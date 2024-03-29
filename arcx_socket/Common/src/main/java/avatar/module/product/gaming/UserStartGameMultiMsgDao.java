package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.UserStartGameMultiMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 玩家开始游戏投币倍率信息数据接口
 */
public class UserStartGameMultiMsgDao {
    private static final UserStartGameMultiMsgDao instance = new UserStartGameMultiMsgDao();
    public static final UserStartGameMultiMsgDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public UserStartGameMultiMsg loadByUserId(int userId) {
        //从缓存获取
        UserStartGameMultiMsg msg = loadCache(userId);
        if(msg==null){
            msg = ProductGamingUtil.initUserStartGameMultiMsg(userId);
            //设置设备的缓存信息
            setCache(userId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取设备房间信息
     */
    private UserStartGameMultiMsg loadCache(int userId) {
        return (UserStartGameMultiMsg) GameData.getCache().get(ProductPrefixMsg.USER_START_GAME_MULTI_MSG+"_"+userId);
    }

    /**
     * 设置设备房间信息
     */
    public void setCache(int userId, UserStartGameMultiMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.USER_START_GAME_MULTI_MSG+"_"+userId, msg);
    }
}
