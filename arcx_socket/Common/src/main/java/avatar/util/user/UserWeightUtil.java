package avatar.util.user;

import avatar.entity.user.product.UserWeightNaInnoEntity;
import avatar.module.user.product.UserPreciseWeightNaInnoDao;
import avatar.util.LogUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家权重工具类
 */
public class UserWeightUtil {
    /**
     * 添加玩家自研权重NA值
     */
    public static void addUserInnoNaNum(int userId, int secondType, long num){
        //查询玩家权重na值信息
        UserWeightNaInnoEntity entity = UserPreciseWeightNaInnoDao.getInstance().loadMsg(userId, secondType);
        if(entity!=null){
            entity.setNaNum(entity.getNaNum()+num);//na值
            //更新
            UserPreciseWeightNaInnoDao.getInstance().update(entity);
        }else{
            LogUtil.getLogger().error("添加玩家{}的{}自研设备{}权重NA值的时候，查询不到对应的玩家自研权重NA值信息------",
                    userId, secondType, num);
        }
    }

    /**
     * 填充玩家自研权重NA信息
     */
    public static UserWeightNaInnoEntity initUserWeightNaInnoEntity(int userId, int secondType) {
        UserWeightNaInnoEntity entity = new UserWeightNaInnoEntity();
        entity.setUserId(userId);//玩家ID
        entity.setSecondType(secondType);//设备二级分类
        entity.setNaNum(0);//na值
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }
}
