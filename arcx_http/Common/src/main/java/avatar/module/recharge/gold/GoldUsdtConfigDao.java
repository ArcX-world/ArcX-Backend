package avatar.module.recharge.gold;

import avatar.entity.recharge.gold.GoldUsdtConfigEntity;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;

/**
 * 金币USDT兑比配置数据接口
 */
public class GoldUsdtConfigDao {
    private static final GoldUsdtConfigDao instance = new GoldUsdtConfigDao();
    public static final GoldUsdtConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public long loadMsg() {
        //从缓存获取
        long proportion = loadCache();
        if(proportion==-1){
            GoldUsdtConfigEntity entity = loadDbMsg();
            proportion = entity==null?0:entity.getProportion();
            //设置缓存
            setCache(proportion);
        }
        return proportion;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private long loadCache(){
        Object obj = GameData.getCache().get(RechargePrefixMsg.GOLD_USDT_CONFIG);
        return obj==null?-1:(long)obj;
    }

    /**
     * 添加缓存
     */
    private void setCache(long proportion){
        GameData.getCache().set(RechargePrefixMsg.GOLD_USDT_CONFIG, proportion);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    public GoldUsdtConfigEntity loadDbMsg() {
        String sql = "select * from gold_usdt_config";
        return GameData.getDB().get(GoldUsdtConfigEntity.class, sql, new Object[]{});
    }


}
