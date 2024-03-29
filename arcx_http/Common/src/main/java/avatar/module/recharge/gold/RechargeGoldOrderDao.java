package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.RechargeGoldOrderEntity;
import avatar.util.GameData;

/**
 * 充值游戏币订单数据接口
 */
public class RechargeGoldOrderDao {
    private static final RechargeGoldOrderDao instance = new RechargeGoldOrderDao();
    public static final RechargeGoldOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(RechargeGoldOrderEntity entity){
        GameData.getDB().insert(entity);
    }
}
