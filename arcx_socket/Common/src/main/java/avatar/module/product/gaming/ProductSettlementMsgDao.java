package avatar.module.product.gaming;

import avatar.data.product.gamingMsg.InnoProductOffLineMsg;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.product.ProductGamingUtil;

/**
 * 设备下机信息数据接口
 */
public class ProductSettlementMsgDao {
    private static final ProductSettlementMsgDao instance = new ProductSettlementMsgDao();
    public static final ProductSettlementMsgDao getInstance(){
        return instance;
    }

    /**
     * 根据设备ID查询
     */
    public InnoProductOffLineMsg loadByProductId(int productId){
        //从缓存获取
        InnoProductOffLineMsg msg = loadCache(productId);
        if(msg==null){
            msg = ProductGamingUtil.initInnoProductOffLineMsg(productId);
            setCache(productId, msg);
        }
        return msg;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private InnoProductOffLineMsg loadCache(int productId) {
        return (InnoProductOffLineMsg) GameData.getCache().get(ProductPrefixMsg.PRODUCT_SETTLEMENT_MSG+"_"+productId);
    }

    /**
     * 设置缓存
     */
    public void setCache(int productId, InnoProductOffLineMsg msg) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SETTLEMENT_MSG+"_"+productId, msg);
    }

}
