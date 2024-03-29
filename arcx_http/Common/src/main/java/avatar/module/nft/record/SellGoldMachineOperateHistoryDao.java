package avatar.module.nft.record;

import avatar.entity.nft.SellGoldMachineOperateHistoryEntity;
import avatar.util.GameData;

/**
 * 售币机经营记录数据接口
 */
public class SellGoldMachineOperateHistoryDao {
    private static final SellGoldMachineOperateHistoryDao instance = new SellGoldMachineOperateHistoryDao();
    public static final SellGoldMachineOperateHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(SellGoldMachineOperateHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
