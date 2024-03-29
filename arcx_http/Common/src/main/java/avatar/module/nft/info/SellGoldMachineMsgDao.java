package avatar.module.nft.info;

import avatar.entity.nft.SellGoldMachineMsgEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.module.nft.user.UserNftListDao;
import avatar.util.GameData;
import avatar.util.system.TimeUtil;

/**
 * 售币机信息数据接口
 */
public class SellGoldMachineMsgDao {
    private static final SellGoldMachineMsgDao instance = new SellGoldMachineMsgDao();
    public static final SellGoldMachineMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public SellGoldMachineMsgEntity loadMsg(String nftCode){
        SellGoldMachineMsgEntity entity = loadCache(nftCode);
        if(entity==null){
            entity = loadDbByNftCode(nftCode);
            if(entity!=null) {
                setCache(nftCode, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private SellGoldMachineMsgEntity loadCache(String nftCode){
        return (SellGoldMachineMsgEntity)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_MSG+"_"+nftCode);
    }

    /**
     * 添加缓存
     */
    private void setCache(String nftCode, SellGoldMachineMsgEntity entity){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_MSG+"_"+nftCode, entity);
    }

    //=========================db===========================

    /**
     * 根据NFT编号查询
     */
    private SellGoldMachineMsgEntity loadDbByNftCode(String nftCode) {
        String sql = "select * from sell_gold_machine_msg where nft_code=?";
        return GameData.getDB().get(SellGoldMachineMsgEntity.class, sql, new Object[]{nftCode});
    }

    /**
     * 更新
     */
    public boolean update(int oriUserId, SellGoldMachineMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //设置缓存
            setCache(entity.getNftCode(), entity);
            //重置营业中售币机列表
            OperateSellGoldMachineListDao.getInstance().removeCache();
            //重置NFT市场列表
            NftMarketListDao.getInstance().removeCache();
            //重置玩家NFT列表
            UserNftListDao.getInstance().removeCache(entity.getUserId());
            if(oriUserId!=entity.getUserId()){
                UserNftListDao.getInstance().removeCache(oriUserId);
            }
        }
        return flag;
    }

}
