package avatar.util.recharge;

import avatar.entity.recharge.superPlayer.SuperPlayerUserMsgEntity;
import avatar.module.recharge.superPlayer.SuperPlayerUserDao;
import avatar.module.recharge.superPlayer.SuperPlayerUserListDao;
import avatar.util.system.TimeUtil;

/**
 * 超级玩家工具类
 */
public class SuperPlayerUtil {
    /**
     * 是否超级玩家
     */
    public static boolean isSuperPlayer(int userId) {
        boolean flag = true;
        //查询玩家信息
        SuperPlayerUserMsgEntity entity = SuperPlayerUserDao.getInstance().loadMsg(userId);
        if(TimeUtil.getNowTime()>TimeUtil.strToLong(entity.getEffectTime())){
            //重置超级玩家缓存
            SuperPlayerUserListDao.getInstance().removeCache();
            flag = false;
        }
        return flag;
    }

    /**
     * 填充超级玩家玩家实体信息
     */
    public static SuperPlayerUserMsgEntity initSuperPlayerUserMsgEntity(int userId) {
        SuperPlayerUserMsgEntity entity = new SuperPlayerUserMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setEffectTime("");//有效时间
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

}
