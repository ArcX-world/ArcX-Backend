package avatar.module.user.product;

import avatar.data.product.gamingMsg.UserLotteryMsg;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 玩家彩票信息数据接口
 */
public class UserLotteryMsgDao {
    private static final UserLotteryMsgDao instance = new UserLotteryMsgDao();
    public static final UserLotteryMsgDao getInstance(){
        return instance;
    }

    /**
     * 根据玩家和分类查询
     */
    public UserLotteryMsg loadByMsg(int userId, int secondLevelType) {
        //从缓存获取
        UserLotteryMsg msg = loadCache(userId, secondLevelType);
        if(msg==null){
            msg = ProductGamingUtil.initUserLotteryMsg(userId, secondLevelType);
            //设置缓存
            setCache(userId, secondLevelType, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public UserLotteryMsg loadCache(int userId, int secondLevelType){
        return (UserLotteryMsg) GameData.getCache().get(UserPrefixMsg.USER_LOTTERY_MSG+"_"+userId+"_"+secondLevelType);
    }

    /**
     * 添加缓存
     */
    public void setCache(int userId, int secondLevelType, UserLotteryMsg msg){
        GameData.getCache().set(UserPrefixMsg.USER_LOTTERY_MSG+"_"+userId+"_"+secondLevelType, msg);
    }

}
