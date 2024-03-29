package avatar.module.recharge.superPlayer;

import avatar.entity.recharge.superPlayer.SuperPlayerOrderEntity;
import avatar.util.GameData;

/**
 * 超级玩家订单信息
 */
public class SuperPlayerOrderDao {
    private static final SuperPlayerOrderDao instance = new SuperPlayerOrderDao();
    public static final SuperPlayerOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(SuperPlayerOrderEntity entity){
        GameData.getDB().insert(entity);
    }

}
