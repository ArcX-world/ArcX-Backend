package avatar.module.recharge.gold;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值金币列表数据接口
 */
public class RechargeGoldListDao {
    private static final RechargeGoldListDao instance = new RechargeGoldListDao();
    public static final RechargeGoldListDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<Integer> loadMsg() {
        //从缓存获取
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.RECHARGE_GOLD_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_GOLD_LIST, list);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select id from recharge_gold_info where active_flag=? order by sequence";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{YesOrNoEnum.YES.getCode()});
        return StrUtil.listSize(list)>0?list:new ArrayList<>();
    }
}
