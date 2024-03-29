package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.HashMap;
import java.util.Map;

/**
 * 自研设备特殊中奖信息数据接口
 */
public class SelfSpecialAwardMsgDao {
    private static final SelfSpecialAwardMsgDao instance = new SelfSpecialAwardMsgDao();
    public static final SelfSpecialAwardMsgDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public Map<Integer, Long> loadByProductId(int productId) {
        //从缓存获取
        Map<Integer, Long> map = loadCache(productId);
        if(map==null){
            map = new HashMap<>();
        }
        //设置缓存
        setCache(productId, map);
        return map;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private Map<Integer, Long> loadCache(int productId){
        return (Map<Integer, Long>) GameData.getCache().get(ProductPrefixMsg.SELF_PRODUCT_SPECIAL_AWARD_MSG+"_"+productId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int productId,Map<Integer, Long> map){
        GameData.getCache().set(ProductPrefixMsg.SELF_PRODUCT_SPECIAL_AWARD_MSG+"_"+productId, map);
    }
}
