package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家ID对应设备唯一ID数据接口
 */
public class UserIdMacDao {
    private static final UserIdMacDao instance = new UserIdMacDao();
    public static final UserIdMacDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public String loadByUserId(int userId){
        String mac = loadCache(userId);
        if(mac==null){
            //查询数据库
            mac = loadDbByUserId(userId);
            //设置缓存
            setCache(userId, mac);
        }
        return mac;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private String loadCache(int userId){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_ID_MAC+"_"+userId);
        return obj==null?null:obj.toString();
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, String mac){
        GameData.getCache().set(UserPrefixMsg.USER_ID_MAC+"_"+userId, mac);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private String loadDbByUserId(int userId) {
        String sql = "select mac from user_mac_msg where user_id=?";
        List<String> list = GameData.getDB().listString(sql, new Object[]{userId});
        return StrUtil.strListSize(list)>0?list.get(0):"";
    }
}
