package avatar.module.product.innoMsg;

import avatar.entity.product.innoMsg.SyncInnoProductIpEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备内推IP数据接口
 */
public class SyncInnoProductIpDao {

    private static final SyncInnoProductIpDao instance = new SyncInnoProductIpDao();
    public static final SyncInnoProductIpDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<SyncInnoProductIpEntity> loadAll() {
        //从缓存获取
        List<SyncInnoProductIpEntity> list = loadCache();
        if(list==null){
            list = loadDbMsg();
            if(list==null){
                list = new ArrayList<>();
            }
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<SyncInnoProductIpEntity> loadCache(){
        return (List<SyncInnoProductIpEntity>) GameData.getCache().get(ProductPrefixMsg.SYNC_INNO_PRODUCT_IP);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<SyncInnoProductIpEntity> entity){
        GameData.getCache().set(ProductPrefixMsg.SYNC_INNO_PRODUCT_IP, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<SyncInnoProductIpEntity> loadDbMsg() {
        String sql = "select * from sync_inno_product_ip";
        return GameData.getDB().list(SyncInnoProductIpEntity.class, sql, new Object[]{});
    }
}
