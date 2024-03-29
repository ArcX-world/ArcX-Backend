package avatar.module.product.energy;

import avatar.entity.product.energy.EnergyExchangeUserHistoryEntity;
import avatar.util.GameData;

/**
 * 能量兑换玩家历史数据接口
 */
public class EnergyExchangeUserHistoryDao {
    private static final EnergyExchangeUserHistoryDao instance = new EnergyExchangeUserHistoryDao();
    public static final EnergyExchangeUserHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public long insert(EnergyExchangeUserHistoryEntity entity){
        return GameData.getDB().insertAndReturn(entity);
    }
}
