package avatar.module.product.energy;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 能量兑换设备数据接口
 */
public class EnergyExchangeProductDao {
    private static final EnergyExchangeProductDao instance = new EnergyExchangeProductDao();
    public static final EnergyExchangeProductDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public int loadMsg(int productId) {
        int configId = loadCache(productId);
        if(configId==-1){
            configId = loadDbByProductId(productId);
            //设置缓存
            setCache(configId, productId);
        }
        return configId;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private int loadCache(int productId){
        Object obj = GameData.getCache().get(ProductPrefixMsg.ENERGY_EXCHANGE_PRODUCT+"_"+productId);
        return obj==null?-1:(int)obj;
    }

    /**
     * 添加缓存
     */
    private void setCache(int productId, int configId){
        GameData.getCache().set(ProductPrefixMsg.ENERGY_EXCHANGE_PRODUCT+"_"+productId, configId);
    }

    //=========================db===========================

    /**
     * 根据设备ID查询
     */
    private int loadDbByProductId(int productId) {
        String sql = "select config_id from energy_exchange_product where product_id=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{productId});
        return StrUtil.listNum(list);
    }

}
