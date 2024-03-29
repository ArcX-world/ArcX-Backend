package avatar.module.product.energy;

import avatar.entity.product.energy.EnergyExchangeUserAwardEntity;
import avatar.util.GameData;

import java.util.List;

/**
 * 能量兑换玩家奖励数据接口
 */
public class EnergyExchangeUserAwardDao {
    private static final EnergyExchangeUserAwardDao instance = new EnergyExchangeUserAwardDao();
    public static final EnergyExchangeUserAwardDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(List<EnergyExchangeUserAwardEntity> list){
        GameData.getDB().insert(list);
    }
}
