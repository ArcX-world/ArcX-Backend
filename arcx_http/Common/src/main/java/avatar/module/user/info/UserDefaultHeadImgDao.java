package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家默认头像数据接口
 */
public class UserDefaultHeadImgDao {
    private static final UserDefaultHeadImgDao instance = new UserDefaultHeadImgDao();
    public static final UserDefaultHeadImgDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public List<String> loadAll(){
        //从缓存获取
        List<String> list = loadCache();
        if(list==null){
            //查询数据库
            list = loadDbAll();
            if(list==null){
                list = new ArrayList<>();
            }
            //设置缓存
            setCache(list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存信息
     */
    private List<String> loadCache() {
        return (List<String>) GameData.getCache().get(UserPrefixMsg.USER_DEFAULT_HEAD_IMG);
    }

    /**
     * 设置缓存
     */
    private void setCache(List<String> list) {
        GameData.getCache().set(UserPrefixMsg.USER_DEFAULT_HEAD_IMG, list);
    }

    //=========================db===========================

    /**
     * 查询所有信息
     */
    private List<String> loadDbAll() {
        String sql = "select img_url from user_default_head_img";
        return GameData.getDB().listString(sql, new Object[]{});
    }
}
