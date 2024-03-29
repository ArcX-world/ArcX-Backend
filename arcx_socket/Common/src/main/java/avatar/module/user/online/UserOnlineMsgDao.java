package avatar.module.user.online;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.LogUtil;
import avatar.util.system.SqlUtil;
import avatar.util.system.TimeUtil;
import avatar.util.user.UserOnlineUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 玩家在线信息
 */
public class UserOnlineMsgDao {
    private static final UserOnlineMsgDao instance = new UserOnlineMsgDao();
    public static final UserOnlineMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public List<UserOnlineMsgEntity> loadByUserId(int userId){
        List<UserOnlineMsgEntity> list = loadCache(userId);
        if(list==null){
            //查询数据库
            list = loadDbByUserId(userId);
            setCache(userId, list);
        }
        return list;
    }

    //=========================cache===========================

    /**
     * 查询缓存
     */
    private List<UserOnlineMsgEntity> loadCache(int userId){
        return (List<UserOnlineMsgEntity>)
                GameData.getCache().get(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    /**
     * 添加缓存
     */
    private void setCache(int userId, List<UserOnlineMsgEntity> list){
        GameData.getCache().set(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId, list);
    }

    /**
     * 重置缓存
     */
    private void removeCache(int userId){
        GameData.getCache().removeCache(UserPrefixMsg.USER_ONLINE_MSG+"_"+userId);
    }

    //=========================db===========================

    /**
     * 查询信息
     */
    private List<UserOnlineMsgEntity> loadDbByUserId(int userId) {
        String sql = "select * from user_online_msg where user_id=? order by create_time desc";
        List<UserOnlineMsgEntity> list = GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{userId});
        return list==null?new ArrayList<>():list;
    }

    /**
     * 添加
     */
    public UserOnlineMsgEntity insert(UserOnlineMsgEntity entity){
        int id = GameData.getDB().insertAndReturn(entity);
        if(id>0){
            entity.setId(id);//id
            //重置缓存
            removeCache(entity.getUserId());
            //重置在线列表信息
            UserOnlineListDao.getInstance().removeCache();
            int productId = entity.getProductId();//设备ID
            //重置设备在线列表信息
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            return entity;
        }else{
            return null;
        }
    }

    /**
     * 更新
     */
    public boolean update(int oriProductId, UserOnlineMsgEntity entity){
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        boolean flag = GameData.getDB().update(entity);
        if(flag){
            //重置缓存
            removeCache(entity.getUserId());
            int productId = entity.getProductId();//设备ID
            //重置设备在线列表信息
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            if(oriProductId>0){
                UserProductOnlineListDao.getInstance().removeCache(oriProductId);
            }
        }
        return flag;
    }

    /**
     * 删除
     */
    public void delete(UserOnlineMsgEntity entity){
        boolean flag = GameData.getDB().delete(entity);
        if (flag) {
            //删除缓存
            removeCache(entity.getUserId());
            //重置在线列表信息
            UserOnlineListDao.getInstance().removeCache();
            int productId = entity.getProductId();//设备ID
            //重置设备在线列表信息
            if(productId>0){
                UserProductOnlineListDao.getInstance().removeCache(productId);
            }
            //重置自增id
            SqlUtil.resetAutoId("user_online_msg");
            //更新玩家经验
            UserOnlineUtil.updateUserExp(entity.getUserId());
        }
    }

    /**
     * 删除
     */
    public void delete(int userId, int productId){
        LogUtil.getLogger().info("删除玩家{}的在线信息-------", userId);
        //查询在线信息
        List<UserOnlineMsgEntity> list = loadByUserId(userId);
        if(list.size()>0) {
            list.forEach(entity->{
                if(entity.getProductId()==productId) {
                    boolean flag = GameData.getDB().delete(entity);
                    if (flag) {
                        //删除缓存
                        removeCache(entity.getUserId());
                        //重置在线列表信息
                        UserOnlineListDao.getInstance().removeCache();
                        //重置设备在线列表信息
                        if (productId > 0) {
                            UserProductOnlineListDao.getInstance().removeCache(productId);
                        }
                        //重置自增id
                        SqlUtil.resetAutoId("user_online_msg");
                        //更新玩家经验
                        UserOnlineUtil.updateUserExp(entity.getUserId());
                    }
                }
            });
        }
    }

    /**
     * 查询所有在线信息
     */
    public List<UserOnlineMsgEntity> loadDbAll() {
        String sql = SqlUtil.loadList("user_online_msg", Collections.singletonList("create_time")).toString();
        return GameData.getDB().list(UserOnlineMsgEntity.class, sql, new Object[]{});
    }
}
