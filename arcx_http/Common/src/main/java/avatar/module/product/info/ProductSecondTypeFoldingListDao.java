package avatar.module.product.info;

import avatar.global.enumMsg.product.ProductStatusEnum;
import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备二级类型折叠设备列表数据接口
 */
public class ProductSecondTypeFoldingListDao {
    private static final ProductSecondTypeFoldingListDao instance = new ProductSecondTypeFoldingListDao();
    public static final ProductSecondTypeFoldingListDao getInstance(){
        return instance;
    }

    /**
     * 根据设备二级分类获取设备列表
     */
    public List<Integer> loadList(int secondType){
        //从缓存获取
        List<Integer> list = loadCache(secondType);
        if(list==null){
            //查询数据库
            list = loadDbBySecondType(secondType);
            if(list==null){
                list = new ArrayList<>();
            }
            //设置缓存
            setCache(secondType, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 通过缓存查询
     */
    private List<Integer> loadCache(int secondType) {
        return (List<Integer>)
                GameData.getCache().get(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType);
    }

    /**
     * 设置缓存
     */
    private void setCache(int secondType, List<Integer> list) {
        GameData.getCache().set(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType, list);
    }

    /**
     * 重置缓存
     */
    public void resetCache(int secondType) {
        GameData.getCache().removeCache(ProductPrefixMsg.PRODUCT_SECOND_TYPE_FOLDING_LIST+"_"+secondType);
    }

    //=========================db===========================

    /**
     * 根据设备类型查询
     */
    private List<Integer> loadDbBySecondType(int secondType) {
        String sql = "select a.id from " +
                " (select id,live_url,sequence,second_sequence,@count :=@count+1 as countNum " +
                " from product_info where second_type=? and status=? order by sequence,second_sequence) a " +
                " group by a.live_url order by a.sequence,a.second_sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondType,
                ProductStatusEnum.NORMAL.getCode()});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
