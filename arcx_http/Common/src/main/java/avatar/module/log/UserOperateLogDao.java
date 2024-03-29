package avatar.module.log;

import avatar.entity.log.UserOperateLogEntity;
import avatar.util.GameData;

/**
 * 玩家操作日志数据接口
 */
public class UserOperateLogDao {
    private static final UserOperateLogDao instance = new UserOperateLogDao();
    public static final UserOperateLogDao getInstance(){
        return instance;
    }

    //=========================db===========================

    public void insert(UserOperateLogEntity entity){
        GameData.getLogDB().insert(entity);
    }
}
