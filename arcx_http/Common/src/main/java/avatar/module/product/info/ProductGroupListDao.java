package avatar.module.product.info;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备号
 */
public class ProductGroupListDao {
    private static final ProductGroupListDao instance = new ProductGroupListDao();
    public static final ProductGroupListDao getInstance(){
        return instance;
    }

    /**
     * 查询设备号信息
     */
    public List<Integer> loadByLiveUrl(String liveUrl) {
        //从缓存获取
        List<Integer> list = loadCache(liveUrl);
        if(list==null){
            //查询数据库
            list = loadDbAll(liveUrl);
            //设置缓存
            setCache(liveUrl, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public List<Integer> loadCache(String liveUrl){
        return (List<Integer>) GameData.getCache().get(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl);
    }

    /**
     * 设置缓存
     */
    public void setCache(String liveUrl, List<Integer> list){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl, list);
    }

    /**
     * 删除设备号
     */
    public void resetCache(String liveUrl) {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_GROUP_LIST+"_"+liveUrl);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbAll(String liveUrl) {
        String sql = "select id from product_info where live_url=? order by sequence,second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{liveUrl});
        if(StrUtil.listSize(list)>0){
            return list;
        }else{
            return new ArrayList<>();
        }
    }

}
