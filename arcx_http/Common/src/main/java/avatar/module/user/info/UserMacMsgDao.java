package avatar.module.user.info;

import avatar.entity.user.info.UserMacMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.prefixMsg.UserPrefixMsg;
import avatar.util.GameData;
import avatar.util.system.StrUtil;

import java.util.List;

/**
 * 玩家设备唯一ID信息数据接口
 */
public class UserMacMsgDao {
    private static final UserMacMsgDao instance = new UserMacMsgDao();
    public static final UserMacMsgDao getInstance(){
        return instance;
    }

    /**
     * 查询缓存信息
     */
    public int loadByMacId(String macId){
        int userId = loadCache(macId);
        if(userId==0){
            //查询数据库
            userId = loadDbByMacId(macId);
            if(userId>0){
                setCache(macId, userId);
            }
        }
        return userId;
    }

    //=========================cache===========================


    /**
     * 查询缓存
     */
    private int loadCache(String mac){
        Object obj = GameData.getCache().get(UserPrefixMsg.USER_MAC_MSG+"_"+mac);
        return obj==null?0:(int) obj;
    }

    /**
     * 添加缓存
     */
    private void setCache(String mac, int userId){
        GameData.getCache().set(UserPrefixMsg.USER_MAC_MSG+"_"+mac, userId);
    }

    /**
     * 删除缓存
     */
    private void removeCache(String mac){
        GameData.getCache().removeCache(UserPrefixMsg.USER_MAC_MSG+"_"+mac);
    }

    //=========================db===========================

    /**
     * 根据macid查询
     */
    private int loadDbByMacId(String macId) {
        String sql = "select user_id from user_mac_msg where mac=? and is_register=? ";
        List<Integer> list = GameData.getDB().listInteger(sql, new Object[]{macId, YesOrNoEnum.YES.getCode()});
        return StrUtil.listNum(list)>0?list.get(0):0;
    }

    /**
     * 添加
     */
    public void insert(UserMacMsgEntity entity) {
        boolean flag = GameData.getDB().insert(entity);
        if(flag){
            //删除缓存
            removeCache(entity.getMac());
            //删除mac对应玩家列表缓存
            MacUserListMsgDao.getInstance().remove(entity.getMac());
        }
    }
}
