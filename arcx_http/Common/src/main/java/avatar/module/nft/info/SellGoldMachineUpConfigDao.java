package avatar.module.nft.info;

import avatar.entity.nft.SellGoldMachineUpConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;

/**
 * 售币机等级配置信息数据接口
 */
public class SellGoldMachineUpConfigDao {
    private static final SellGoldMachineUpConfigDao instance = new SellGoldMachineUpConfigDao();
    public static final SellGoldMachineUpConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public SellGoldMachineUpConfigEntity loadMsg(int lv){
        SellGoldMachineUpConfigEntity entity = loadCache(lv);
        if(entity==null){
            entity = loadDbByLv(lv);
            if(entity!=null) {
                setCache(lv, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private SellGoldMachineUpConfigEntity loadCache(int lv){
        return (SellGoldMachineUpConfigEntity)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_LV_CONFIG+"_"+lv);
    }

    /**
     * 添加缓存
     */
    private void setCache(int lv, SellGoldMachineUpConfigEntity entity){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_LV_CONFIG+"_"+lv, entity);
    }

    //=========================db===========================

    /**
     * 根据等级查询
     */
    private SellGoldMachineUpConfigEntity loadDbByLv(int lv) {
        String sql = "select * from sell_gold_machine_up_config where lv=?";
        return GameData.getDB().get(SellGoldMachineUpConfigEntity.class, sql, new Object[]{lv});
    }

}
