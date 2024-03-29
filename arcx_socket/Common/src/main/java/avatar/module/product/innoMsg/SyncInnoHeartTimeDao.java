package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

/**
 * 自研设备心跳时间数据接口
 */
public class SyncInnoHeartTimeDao {
    private static final SyncInnoHeartTimeDao instance = new SyncInnoHeartTimeDao();
    public static final SyncInnoHeartTimeDao getInstance(){
        return instance;
    }

    /**
     * 获取信息
     */
    public long loadTime(String linkMsg) {
        //从缓存获取
        long time = loadCache(linkMsg);
        if(time==-1){
            time = 0;
            setCache(linkMsg, 0);
        }
        return time;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public long loadCache(String linkMsg){
        Object obj = GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_HEART_TIME+"_"+linkMsg);
        return obj==null?-1:Long.parseLong(obj.toString());
    }

    /**
     * 添加缓存
     */
    public void setCache(String linkMsg, long time){
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_HEART_TIME+"_"+linkMsg, time);
    }
}
