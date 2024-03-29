package avatar.module.user.attribute;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家属性配置数据接口
 */
public class UserGameExpConfigDao {
    private static final UserGameExpConfigDao instance = new UserGameExpConfigDao();
    public static final UserGameExpConfigDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public long loadMsg(){
        long coinNum = loadCache();
        if(coinNum==-1){
            coinNum = loadDbCoinNum();
            setCache(coinNum);
        }
        return coinNum;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private long loadCache(){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_GAME_EXP_CONFIG);
        return obj==null?-1:(long) obj;
    }

    /**
     * 添加缓存
     */
    private void setCache(long coinNum){
        GameData.getCache().set(UserPrefixMsg.USER_GAME_EXP_CONFIG, coinNum);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private long loadDbCoinNum() {
        String sql = "select coin_num from user_game_exp_config";
        List<String> list = GameData.getDB().listString(sql, new Object[]{});
        return Long.parseLong(StrUtil.strListNum(list));
    }


}
