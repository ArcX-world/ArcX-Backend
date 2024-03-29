package avatar.module.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备中奖信息数据接口
 */
public class SelfProductAwardMsgDao {
    private static final SelfProductAwardMsgDao instance = new SelfProductAwardMsgDao();
    public static final SelfProductAwardMsgDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public List<Long> loadByProductId(int productId) {
        //从缓存获取
        List<Long> list = loadCache(productId);
        if(list==null){
            list = new ArrayList<>();
        }
        //设置缓存
        setCache(productId, list);
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Long> loadCache(int productId){
        return (List<Long>) GameData.getCache().get(ProductPrefixMsg.SELF_PRODUCT_AWARD_MSG+"_"+productId);
    }

    /**
     * 添加缓存
     */
    public void setCache(int productId, List<Long> list){
        GameData.getCache().set(ProductPrefixMsg.SELF_PRODUCT_AWARD_MSG+"_"+productId, list);
    }
}
