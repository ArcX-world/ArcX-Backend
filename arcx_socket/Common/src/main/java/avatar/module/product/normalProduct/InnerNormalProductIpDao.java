package avatar.module.product.normalProduct;

import avatar.entity.product.normalProduct.InnerNormalProductIpEntity;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通设备内推IP
 */
public class InnerNormalProductIpDao {

    private static final InnerNormalProductIpDao instance = new InnerNormalProductIpDao();
    public static final InnerNormalProductIpDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<InnerNormalProductIpEntity> loadAll() {
        //从缓存获取
        List<InnerNormalProductIpEntity> list = loadCache();
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
    private List<InnerNormalProductIpEntity> loadCache(){
        return (List<InnerNormalProductIpEntity>) GameData.getCache().get(ProductPrefixMsg.INNER_NORMAL_PRODUCT_IP);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<InnerNormalProductIpEntity> entity){
        GameData.getCache().set(ProductPrefixMsg.INNER_NORMAL_PRODUCT_IP, entity);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<InnerNormalProductIpEntity> loadDbMsg() {
        String sql = SqlUtil.loadList("inner_normal_product_ip", new ArrayList<>()).toString();
        return GameData.getDB().list(InnerNormalProductIpEntity.class, sql, new Object[]{});
    }
}
