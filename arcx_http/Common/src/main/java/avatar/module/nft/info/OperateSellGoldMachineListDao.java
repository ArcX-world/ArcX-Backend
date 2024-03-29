package avatar.module.nft.info;

import avatar.entity.nft.NftConfigEntity;
import avatar.global.prefixMsg.NftPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 营业中售币机列表
 */
public class OperateSellGoldMachineListDao {
    private static final OperateSellGoldMachineListDao instance = new OperateSellGoldMachineListDao();
    public static final OperateSellGoldMachineListDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<String> loadMsg(){
        List<String> list = loadCache();
        if(list==null){
            list = loadDbList();
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
        return (List<String>)
                GameData.getCache().get(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<String> list){
        GameData.getCache().set(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST, list);
    }

    /**
     * 删除缓存
     */
    public void removeCache(){
        GameData.getCache().removeCache(NftPrefixMsg.OPERATE_SELL_GOLD_MACHINE_LIST);
    }

    //=========================db===========================

    /**
     * 查询列表
     */
    private List<String> loadDbList() {
        //查询配置信息
        NftConfigEntity entity = NftConfigDao.getInstance().loadMsg();
        long adWeight = entity==null?0:entity.getAdWeight();//广告权重系数
        long saleWeight = entity==null?0:entity.getSaleWeight();//出售权重系数
        long nowTime = TimeUtil.getNowTime();//当前时间
        String sql = "select nft_code from sell_gold_machine_msg where status=4 " +
                "order by ((?-UNIX_TIMESTAMP(start_operate_time))*(?-UNIX_TIMESTAMP(start_operate_time))/60000.0+" +
                "adv*?-sell_time*?) desc";
        List<String> list = GameData.getDB().listString(sql, new Object[]{nowTime, nowTime, adWeight, saleWeight});
        return StrUtil.strRetList(list);
    }
}
