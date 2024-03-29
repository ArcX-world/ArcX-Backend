package avatar.module.product.innoMsg;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 自研设备重连状态数据接口
 */
public class SyncInnoReconnectDao {
    private static final SyncInnoReconnectDao instance = new SyncInnoReconnectDao();
    public static final SyncInnoReconnectDao getInstance(){
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
        Object object = GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_RECONNECT+fromKey+tokey);
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
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_RECONNECT+fromKey+tokey, status);
    }
}
