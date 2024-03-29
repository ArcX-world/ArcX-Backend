package avatar.module.recharge.property;

import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.RechargePrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 充值道具列表数据接口
 */
public class RechargePropertyListDao {
    private static final RechargePropertyListDao instance = new RechargePropertyListDao();
    public static final RechargePropertyListDao getInstance(){
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
        return (List<Integer>) GameData.getCache().get(RechargePrefixMsg.RECHARGE_PROPERTY_LIST);
    }

    /**
     * 添加缓存
     */
    private void setCache(List<Integer> list){
        GameData.getCache().set(RechargePrefixMsg.RECHARGE_PROPERTY_LIST, list);
    }


    //=========================db===========================

    /**
     * 查询信息
     */
    private List<Integer> loadDbMsg() {
        String sql = "select id from recharge_property_msg where active_flag=? order by create_time";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{YesOrNoEnum.YES.getCode()});
        return StrUtil.retList(list);
    }
}
