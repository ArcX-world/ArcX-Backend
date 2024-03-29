package avatar.module.recharge.property;

import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.recharge.RechargePropertyUtil;

import java.util.List;

/**
 * 充值道具列表数据接口
 */
public class RechargePropertyOnlineListDao {
    private static final RechargePropertyOnlineListDao instance = new RechargePropertyOnlineListDao();
    public static final RechargePropertyOnlineListDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadMsg() {
        //从缓存获取
        List<Integer> list = loadCache();
        if(list==null){
            list = RechargePropertyUtil.loadOnlineList();
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_ONLINE_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_ONLINE_LIST, list);
    }


}
