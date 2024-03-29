package avatar.module.user.info;

import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 邮箱玩家
 */
public class EmailUserDao {
    private static final EmailUserDao instance = new EmailUserDao();
    public static final EmailUserDao getInstance(){
        return instance;
    }

    /**
     * 查询信息
     */
    public int loadMsg(String email){
        //从缓存查找
        int userId = loadCache(email);
        if(userId==-1){
            //查询数据库
            userId = loadDbByEmail(email);
            //设置缓存
            setCache(email, userId);
        }
        return userId;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private int loadCache(String email){
        Object obj = GameData.getCache().get(UserPrefixMsg.EMAIL_USER+"_"+email);
        return obj==null?-1:(int) obj;
    }

    /**
     * 添加缓存
     */
    public void setCache(String email, int userId){
        GameData.getCache().set(UserPrefixMsg.EMAIL_USER+"_"+email, userId);
    }

    //=========================db===========================

    /**
     * 根据邮件查询
     */
    private int loadDbByEmail(String email) {
        String sql = "select id from user_info where email=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{email});
        return StrUtil.listNum(list);
    }
}
