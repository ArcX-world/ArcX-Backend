package avatar.module.nft.info;

import avatar.entity.nft.NftConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;

/**
 * NFT配置信息数据接口
 */
public class NftConfigDao {
    private static final NftConfigDao instance = new NftConfigDao();
    public static final NftConfigDao getInstance() {
        return instance;
    }

    /**
     * 查询信息
     */
    public NftConfigEntity loadMsg() {
        //从缓存获取
        NftConfigEntity entity = loadCache();
        if (entity==null) {
            //查询数据库
            entity = loadDbMsg();
            //设置缓存
            if (entity != null) {
                setCache(entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private NftConfigEntity loadCache() {
        return (NftConfigEntity)
                GameData.getCache().get(NftPrefixMsg.NFT_CONFIG);
    }

    /**
     * 设置缓存
     *
     */
    private void setCache(NftConfigEntity entity) {
        GameData.getCache().set(NftPrefixMsg.NFT_CONFIG, entity);
    }

    //=========================db===========================

    /**
     * 查询配置信息
     */
    private NftConfigEntity loadDbMsg() {
        String sql = "select * from nft_config";
        return GameData.getDB().get(NftConfigEntity.class, sql, new Object[]{});
    }

}
