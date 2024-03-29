package avatar.util.user;

import avatar.global.lockMsg.LockMsg;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.system.ParamsUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 玩家在线信息工具类
 */
public class UserOnlineUtil {
    /**
     * 更新在线信息在线
     */
    public static void onlineMsgOnline(int userId, String localIp, int localPort) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                //查询在线信息
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()==0){
                    UserOnlineMsgDao.getInstance().insert(initUserOnlineMsgEntity(userId, localIp, localPort, 0));
                }else{
                    list.forEach(entity->{
                        if(entity.getIsOnline()== YesOrNoEnum.NO.getCode()){
                            entity.setIsOnline(YesOrNoEnum.YES.getCode());
                            UserOnlineMsgDao.getInstance().update(0, entity);
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 填充玩家在线信息
     */
    private static UserOnlineMsgEntity initUserOnlineMsgEntity(int userId, String ip, int port, int productId){
        UserOnlineMsgEntity entity = new UserOnlineMsgEntity();
        entity.setUserId(userId);//玩家ID
        entity.setProductId(productId);//设备ID
        entity.setIsOnline(YesOrNoEnum.YES.getCode());//是否在线：是
        entity.setIsGaming(YesOrNoEnum.NO.getCode());//是否游戏中：否
        entity.setIp(ip);//ip
        entity.setPort(port+"");//端口
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        entity.setUpdateTime(TimeUtil.getNowTimeStr());//更新时间
        return entity;
    }

    /**
     * 删除玩家在线信息
     */
    public static void delUserOnlineMsg(int userId) {
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                //只删除不在线信息
                if(!ParamsUtil.isConfirm(entity.getIsGaming())){
                    UserOnlineMsgDao.getInstance().delete(entity);
                }
            });
        }
    }

    /**
     * 玩家是否在线
     */
    public static boolean isOnline(int userId) {
        //查询玩家在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()!=0 && list.get(0).getIsOnline() == YesOrNoEnum.YES.getCode();
    }

}
