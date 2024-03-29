package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.DollGamingMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 娃娃机游戏信息数据接口
 */
public class DollGamingMsgDao {
    private static final DollGamingMsgDao instance = new DollGamingMsgDao();
    public static final DollGamingMsgDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID获取堆塔信息
     */
    public DollGamingMsg loadByProductId(int productId) {
        //从缓存获取
        DollGamingMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initDollGamingMsg(productId);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取堆塔信息
     */
    private DollGamingMsg loadCache(int productId) {
        return (DollGamingMsg) GameData.getCache().get(ProductPrefixMsg.DOLL_GAMING_MSG+"_"+productId);
    }

    /**
     * 设置自动发炮信息
     */
    public void setCache(int productId, DollGamingMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.DOLL_GAMING_MSG+"_"+productId, msg);
    }
}
