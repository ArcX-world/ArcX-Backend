package avatar.util.user;

import avatar.entity.user.info.UserUsdtBalanceEntity;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.info.UserUsdtBalanceDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**
 * 玩家USDT工具类
 */
public class UserUsdtUtil {
    /**
     * 填充玩家USDT余额信息
     */
    public static UserUsdtBalanceEntity initUserUsdtBalanceEntity(int userId) {
        UserUsdtBalanceEntity entity = new UserUsdtBalanceEntity();
        entity.setUserId(userId);//玩家ID
        entity.setNum(0);//usdt余额
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime("");//更新时间
        return entity;
    }

    /**
     * 获取USDT余额
     */
    public static double usdtBalance(int userId){
        //查询信息
        UserUsdtBalanceEntity entity = UserUsdtBalanceDao.getInstance().loadByMsg(userId);
        return entity==null?0:entity.getNum();
    }

    /**
     * 添加玩家余额
     */
    public static boolean addUsdtBalance(int userId, double num) {
        if(num>0) {
            boolean flag = false;
            //获取玩家余额锁
            RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USDT_COST_LOCK + "_" + userId,
                    2000);
            try {
                if (lock.lock()) {
                    //查询信息
                    UserUsdtBalanceEntity entity = UserUsdtBalanceDao.getInstance().loadByMsg(userId);
                    if (entity != null) {
                        entity.setNum(StrUtil.truncateFourDecimal(entity.getNum() + num));//商品数量
                        flag = UserUsdtBalanceDao.getInstance().update(entity);
                    } else {
                        LogUtil.getLogger().error("添加玩家{}USDT数量{}的时候，查询不到玩家USDT余额信息-----", userId, num);
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
