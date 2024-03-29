package avatar.util.user;

import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserPropertyMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家道具工具类
 */
public class UserPropertyUtil {
    /**
     * 填充玩家道具实体信息
     */
    public static UserPropertyMsgEntity initUserPropertyMsgEntity(int userId, int propertyType) {
        UserPropertyMsgEntity entity = new UserPropertyMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setPropertyType(propertyType);//道具类型
        entity.setNum(0);//道具数量
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

    /**
     * 获取道具余额
     */
    public static long getUserProperty(int userId, int propertyType) {
        //查询信息
        UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
        return entity==null?0:entity.getNum();
    }

    /**
     * 添加道具
     */
    public static boolean addUserProperty(int userId, int propertyType, int num) {
        if(num>0) {
            boolean flag = false;
            //获取玩家余额锁
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    //查询信息
                    UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                    if (entity != null) {
                        entity.setNum(entity.getNum() + num);//商品数量
                        flag = UserPropertyMsgDao.getInstance().update(entity);
                    } else {
                        LogUtil.getLogger().error("添加玩家{}-{}-{}道具的时候，查询不到玩家道具余额信息-----", userId,
                                PropertyTypeEnum.getNameByCode(propertyType), num);
                    }
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
            } finally {
                lock.unlock();
            }
            return flag;
        }else{
            return true;
        }
    }
}
