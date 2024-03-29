package avatar.module.user.info;

import avatar.entity.user.info.UserInfoEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 玩家信息数据接口
 */
public class UserInfoDao {
    private static final UserInfoDao instance = new UserInfoDao();
    public static final UserInfoDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public UserInfoEntity loadByUserId(int userId){
        //从缓存查找
        UserInfoEntity entity = loadCache(userId);
        if(entity==null){
            //查询数据库
            entity = loadDbByUserId(userId);
            //更新缓存
            if(entity!=null){
                setCache(userId, entity);
            }
        }
        return entity;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private UserInfoEntity loadCache(int userId){
        return (UserInfoEntity) GameData.getCache().get(UserPrefixMsg.USER_INFO+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, UserInfoEntity entity){
        GameData.getCache().set(UserPrefixMsg.USER_INFO+"_"+userId, entity);
    }

    //=========================db===========================

    /**
     * 根据玩家ID查询
     */
    private UserInfoEntity loadDbByUserId(int userId) {
        return GameData.getDB().get(UserInfoEntity.class, userId);
    }

    /**
     * 添加
     */
    public int insert(UserInfoEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            //重置总玩家缓存
            TotalUserListDao.getInstance().removeCache();
            if(!StrUtil.checkEmpty(entity.getEmail())){
                //邮箱玩家
                EmailUserDao.getInstance().setCache(entity.getEmail(), id);
            }
        }
        return id;
    }

    /**
     * 更新
     */
    public boolean update(UserInfoEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //更新缓存
            setCache(entity.getId(), entity);
        }
        return flag;
    }

    /**
     * 根据昵称查询
     */
    public UserInfoEntity loadDbByNickName(String nickName) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("nick_name", nickName);//昵称
        String sql = SqlUtil.getSql("user_info", paramsMap).toString();
        return GameData.getDB().get(UserInfoEntity.class, sql, new Object[]{});
    }

    /**
     * 根据昵称查询
     */
    public List<Integer> loadDbUserList(String nickName) {
        String sql = "select id from user_info where nick_name=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{nickName});
        return StrUtil.retList(list);
    }

    /**
     * 根据IP查询玩家ID
     */
    public List<Integer> loadDbByIp(String forbidIp) {
        String sql = "select id from user_info where ip=?";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{forbidIp});
        return StrUtil.listNum(list)>0?list:new ArrayList<>();
    }

}
