package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.PileTowerMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 堆塔信息数据接口
 */
public class PileTowerMsgDao {
    private static final PileTowerMsgDao instance = new PileTowerMsgDao();
    public static final PileTowerMsgDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID获取堆塔信息
     */
    public PileTowerMsg loadByProductId(int productId) {
        //从缓存获取
        PileTowerMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initPileTowerMsg(productId);
            //设置缓存信息
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 获取堆塔信息
     */
    private PileTowerMsg loadCache(int productId) {
        return (PileTowerMsg) GameData.getCache().get(ProductPrefixMsg.PILE_TOWER_MSG+"_"+productId);
    }

    /**
     * 设置自动发炮信息
     */
    public void setCache(int productId, PileTowerMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PILE_TOWER_MSG+"_"+productId, msg);
    }
}
