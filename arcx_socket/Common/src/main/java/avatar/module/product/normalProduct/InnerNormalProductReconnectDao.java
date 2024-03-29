package avatar.module.product.normalProduct;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 普通设备内部重连状态数据接口
 */
public class InnerNormalProductReconnectDao {
    private static final InnerNormalProductReconnectDao instance = new InnerNormalProductReconnectDao();
    public static final InnerNormalProductReconnectDao getInstance(){
        return instance;
    }

    /**
     * 根据IP端口查询重连状态
     */
    public int loadMsg(String fromKey, String toKey) {
        //从缓存获取
        return loadCache(fromKey, toKey);
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private int loadCache(String fromKey, String tokey){
        int status = YesOrNoEnum.NO.getCode();//是否重连：否
        Object object = GameData.getCache().get(ProductPrefixMsg.INNER_NORMAL_PRODUCT_RECONNECT+fromKey+tokey);
        if(object==null){
            setCache(fromKey, tokey, status);
        }else{
            status = (int) object;
        }
        return status;
    }

    /**
     * 添加缓存
     */
    public void setCache(String fromKey, String tokey, int status){
        GameData.getCache().set(ProductPrefixMsg.INNER_NORMAL_PRODUCT_RECONNECT+fromKey+tokey, status);
    }
}
