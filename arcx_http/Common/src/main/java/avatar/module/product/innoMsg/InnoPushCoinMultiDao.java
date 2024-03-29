package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 自研设备投币倍率信息数据接口
 */
public class InnoPushCoinMultiDao {
    private static final InnoPushCoinMultiDao instance = new InnoPushCoinMultiDao();
    public static final InnoPushCoinMultiDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadBySecondType(int secondType) {
        //从缓存获取
        List<Integer> list = loadCache(secondType);
        if(list==null){
            //从数据库查询
            list = loadDbBySecondType(secondType);
            //设置缓存
            setCache(secondType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<Integer> loadCache(int secondType){
        return (List<Integer>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_MULTI+"_"+secondType);
    }

    /**
     * 添加缓存
     */
    private void setCache(int secondType, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_PUSH_COIN_MULTI+"_"+secondType, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<Integer> loadDbBySecondType(int secondType) {
        String sql = "select coin_num from inno_push_coin_multi_msg where second_type=? order by coin_num desc";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondType});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
