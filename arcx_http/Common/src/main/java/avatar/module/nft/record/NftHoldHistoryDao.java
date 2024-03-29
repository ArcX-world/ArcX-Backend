package avatar.module.nft.record;

import avatar.entity.nft.NftHoldHistoryEntity;
import avatar.util.GameData;

/**
 * NFT持有记录数据接口
 */
public class NftHoldHistoryDao {
    private static final NftHoldHistoryDao instance = new NftHoldHistoryDao();
    public static final NftHoldHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(NftHoldHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }
}
