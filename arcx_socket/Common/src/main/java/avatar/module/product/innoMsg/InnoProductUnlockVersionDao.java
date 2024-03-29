package avatar.module.product.innoMsg;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 自研设备取消锁定版本数据接口
 */
public class InnoProductUnlockVersionDao {
    private static final InnoProductUnlockVersionDao instance = new InnoProductUnlockVersionDao();
    public static final InnoProductUnlockVersionDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<String> loadMsg() {
        //从缓存获取
        List<String> list = loadCache();
        if(list==null){
            //从数据库查询
            list = loadDbMsg();
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<String> loadCache(){
        return (List<String>) GameData.getCache().get(
                ProductPrefixMsg.INNO_PRODUCT_UNLOCK_VERSION);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<String> list){
        GameData.getCache().set(ProductPrefixMsg.INNO_PRODUCT_UNLOCK_VERSION, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<String> loadDbMsg() {
        String sql = "select version_code from inno_product_unlock_version";
        List<String> list = GameData.getDB().listString(sql, new Object[]{});
        return StrUtil.strRetList(list);
    }
}
