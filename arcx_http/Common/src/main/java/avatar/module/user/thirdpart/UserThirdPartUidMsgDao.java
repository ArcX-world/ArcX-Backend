package avatar.module.user.thirdpart;

import avatar.entity.user.thirdpart.UserThirdPartUidMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家第三方唯一ID信息数据接口
 */
public class UserThirdPartUidMsgDao {
    private static final UserThirdPartUidMsgDao instance = new UserThirdPartUidMsgDao();
    public static final UserThirdPartUidMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public int loadByUid(String uid){
        int userId = loadCache(uid);
        if(userId==0){
            //查询数据库
            userId = loadDbByUid(uid);
            if(userId>0){
                setCache(uid, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================


    /**
     * 查询缓存
     */
    private int loadCache(String uid){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_UID_MSG+"_"+uid);
        return obj==null?0:(int) obj;
    }

    /**
     * 添加缓存
     */
    private void setCache(String uid, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_UID_MSG+"_"+uid, userId);
    }

    //=========================db===========================

    /**
     * 根据uid查询
     */
    private int loadDbByUid(String uid) {
        String sql = "select user_id from user_third_part_uid_msg where uid=? ";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{uid});
        return StrUtil.listNum(list)>0?list.get(0):0;
    }

    /**
     * 添加
     */
    public void insert(UserThirdPartUidMsgEntity entity) {
        boolean flag = GameData.getDB().insert(entity);
        if(flag){
            //设置缓存
            setCache(entity.getUid(), entity.getUserId());
        }
    }
}
