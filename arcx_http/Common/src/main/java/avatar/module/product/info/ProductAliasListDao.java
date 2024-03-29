package avatar.module.product.info;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备号
 */
public class ProductAliasListDao {
    private static final ProductAliasListDao instance = new ProductAliasListDao();
    public static final ProductAliasListDao getInstance(){
        return instance;
    }

    /**
     * 查询设备号信息
     */
    public List<String> loadAll() {
        //从缓存获取
        List<String> list = loadCache();
        if(list==null || list.size()==0){
            //查询数据库
            list = loadDbAll();
            //添加缓存
            if(list!=null && list.size()>0){
                setCache(list);
            }
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public List<String> loadCache(){
        return (List<String>) GameData.getCache().get(ProductPrefixMsg.PRODUCT_ALIAS_LIST);
    }

    /**
     * 设置缓存
     */
    public void setCache(List<String> list){
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_ALIAS_LIST, list);
    }

    /**
     * 添加缓存
     */
    public void addCache(String alias){
        List<String> list = loadAll();
        if(list==null){
            list = new ArrayList<>();
        }
        if(!list.contains(alias)){
            list.add(alias);
        }
        setCache(list);
    }

    /**
     * 删除设备号
     */
    public void resetCache() {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_ALIAS_LIST);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<String> loadDbAll() {
        String sql = "select alias from product_info";
        return GameData.getDB().listString(sql, new Object[]{});
    }

}
