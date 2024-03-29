package avatar.module.product.productType;

import avatar.global.prefixMsg.ProductPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 彩票游戏币对比数据接口
 */
public class LotteryCoinPercentDao {
    private static final LotteryCoinPercentDao instance = new LotteryCoinPercentDao();
    public static final LotteryCoinPercentDao getInstance(){
        return instance;
    }

    /**
     * 根据二级分类查询
     */
    public int loadBySecondLevelType(int secondLevelType) {
        //从缓存获取
        int percent = loadCache(secondLevelType);
        if(percent==-1){
            //查询数据库
            percent = loadDbBySecondLevelType(secondLevelType);
            //设置缓存
            setCache(secondLevelType, percent);
        }
        return percent;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    public int loadCache(int secondLevelType){
        Object obj = GameData.getCache().get(ProductPrefixMsg.LOTTERY_COIN_PERCENT+"_"+secondLevelType);
        if(obj==null){
            return -1;
        }else{
            return (int) obj;
        }
    }

    /**
     * 添加缓存
     */
    public void setCache(int secondLevelType, int percent){
        GameData.getCache().set(ProductPrefixMsg.LOTTERY_COIN_PERCENT+"_"+secondLevelType, percent);
    }

    //=========================db===========================

    /**
     * 根据设备二级分类查询
     */
    private int loadDbBySecondLevelType(int secondLevelType) {
        String sql = "select percent from lottery_coin_percent where second_level_type=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{secondLevelType});
        return StrUtil.listNum(list);
    }

}
