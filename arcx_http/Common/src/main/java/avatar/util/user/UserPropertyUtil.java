package avatar.util.user;

import avatar.data.user.balance.PropertyKnapsackMsg;
import avatar.entity.basic.systemMsg.PropertyMsgEntity;
import avatar.entity.user.info.UserPropertyMsgEntity;
import avatar.global.enumMsg.basic.commodity.PropertyTypeEnum;
import avatar.global.enumMsg.system.ClientCode;
import avatar.global.lockMsg.LockMsg;
import avatar.module.basic.systemMsg.PropertyListDao;
import avatar.module.basic.systemMsg.PropertyMsgDao;
import avatar.module.user.info.UserPropertyMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.task.user.PropertyUserAwardTask;
import avatar.util.LogUtil;
import avatar.util.basic.MediaUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

import java.util.List;

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

    /**
     * 扣除玩家道具
     */
    public static boolean costUserProperty(int userId, int propertyType, int costNum) {
        boolean flag = false;
        if(costNum==0){
            return true;
        }
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PROPERTY_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                if(getUserProperty(userId, propertyType)>=costNum) {
                    //查询信息
                    UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                    if (entity != null) {
                        long num = entity.getNum();//道具数量
                        entity.setNum(Math.max(0, (num - costNum)));//商品数量
                        flag = UserPropertyMsgDao.getInstance().update(entity);
                    } else {
                        LogUtil.getLogger().error("扣除玩家{}-{}-{}道具的时候，查询不到玩家平台余额信息-----", userId,
                                PropertyTypeEnum.getNameByCode(propertyType), costNum);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }

    /**
     * 道具背包
     */
    public static void propertyKnapsack(int userId, List<Integer> list, List<PropertyKnapsackMsg> retList) {
        if(list.size()>0){
            list.forEach(propertyType->{
                //查询玩家道具信息
                UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
                if(entity.getNum()>0){
                    retList.add(initPropertyKnapsackMsg(propertyType, entity.getNum()));
                }
            });
        }
    }

    /**
     * 填充道具背包信息
     */
    private static PropertyKnapsackMsg initPropertyKnapsackMsg(int propertyType, long num) {
        PropertyKnapsackMsg msg = new PropertyKnapsackMsg();
        //查询道具信息
        PropertyMsgEntity entity = PropertyMsgDao.getInstance().loadMsg(propertyType);
        msg.setPptTp(propertyType);//道具类型
        msg.setNm(entity==null?"":entity.getName());//道具名称
        msg.setDsc(entity==null?"":entity.getDesc());//描述
        msg.setPct(entity==null?"":MediaUtil.getMediaUrl(entity.getImgUrl()));//道具图片
        msg.setPpyAmt(num);//道具数量
        return msg;
    }

    /**
     * 使用道具
     */
    public static int useProperty(int userId, int propertyType) {
        int status = ClientCode.PROPERTY_NO_ENOUGH.getCode();//道具数量不足
        //查询道具信息
        UserPropertyMsgEntity entity = UserPropertyMsgDao.getInstance().loadByMsg(userId, propertyType);
        if(entity!=null && entity.getNum()>0){
            //查询在线道具列表
            if(PropertyListDao.getInstance().loadMsg().contains(propertyType)){
                boolean flag = costUserProperty(userId, propertyType, 1);
                if(flag){
                    status = ClientCode.SUCCESS.getCode();//成功
                }
            }else{
                status = ClientCode.INVALID_COMMODITY.getCode();//无效商品
            }
        }
        if(ParamsUtil.isSuccess(status)){
            //添加使用道具奖励
            SchedulerSample.delayed(1, new PropertyUserAwardTask(userId, propertyType));
        }
        return status;
    }
}
