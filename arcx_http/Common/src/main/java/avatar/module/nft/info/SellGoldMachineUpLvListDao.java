package avatar.module.nft.info;

import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 售币机升级等级列表数据接口
 */
public class SellGoldMachineUpLvListDao {
    private static final SellGoldMachineUpLvListDao instance = new SellGoldMachineUpLvListDao();
    public static final SellGoldMachineUpLvListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<Integer> loadMsg(){
        List<Integer> list = loadCache();
        if(list==null){
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
    private List<Integer> loadCache(){
        return (List<Integer>)
                GameData.getCache().get(NftPrefixMsg.SELL_GOLD_MACHINE_UP_LV_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(NftPrefixMsg.SELL_GOLD_MACHINE_UP_LV_LIST, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select lv from sell_gold_machine_up_config order by lv";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{});
        return StrUtil.retList(list);
    }
}
