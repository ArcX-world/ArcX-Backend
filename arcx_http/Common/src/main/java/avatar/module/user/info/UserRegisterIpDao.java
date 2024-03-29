package avatar.module.user.info;

import avatar.entity.user.info.UserRegisterIpEntity;
import avatar.util.GameData;

/**
 * 玩家注册IP数据接口
 */
public class UserRegisterIpDao {
    private static final UserRegisterIpDao instance = new UserRegisterIpDao();
    public static final UserRegisterIpDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public void insert(UserRegisterIpEntity entity){
        GameData.getDB().insert(entity);
    }

    /**
     * 根据玩家ID查询
     */
    public UserRegisterIpEntity loadDbByUserId(int userId) {
        String sql = "select * from user_register_ip where user_id=?";
        return GameData.getDB().get(UserRegisterIpEntity.class, sql, new Object[]{userId});
    }

    /**
     * 更新
     */
    public void update(UserRegisterIpEntity entity) {
        GameData.getDB().update(entity);
    }
}
