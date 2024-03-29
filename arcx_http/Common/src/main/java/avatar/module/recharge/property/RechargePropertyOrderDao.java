package avatar.module.recharge.property;

import avatar.entity.recharge.property.RechargePropertyOrderEntity;
import avatar.util.GameData;

/**
 * 道具充值订单数据接口
 */
public class RechargePropertyOrderDao {
    private static final RechargePropertyOrderDao instance = new RechargePropertyOrderDao();
    public static final RechargePropertyOrderDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(RechargePropertyOrderEntity entity){
        GameData.getDB().insert(entity);
    }

}
