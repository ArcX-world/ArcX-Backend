package avatar.module.product.socket;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 互通设备socket连接数据接口
 */
public class ConnectSocketLinkDao {
    private static final ConnectSocketLinkDao instance = new ConnectSocketLinkDao();
    public static final ConnectSocketLinkDao getInstance(){
        return instance;
    }

    /**
     * 根据IP查询连接时间
     */
    public long loadByLinkMsg(String linkMsg){
        //从缓存获取
        return loadCache(linkMsg);
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private long loadCache(String linkMsg) {
        return GameData.getCache().get(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg)==null?0:
                (long) GameData.getCache().get(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg);
    }

    /**
     * 设置缓存
     */
    public void setCache(String linkMsg, long time) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_CONNECT_TIME+"_"+linkMsg, time);
    }
}
