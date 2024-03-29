package avatar.util.user;

import avatar.data.user.attribute.UserOnlineExpMsg;
import avatar.entity.user.attribute.UserAttributeMsgEntity;
import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.user.attribute.UserAttributeMsgDao;
import avatar.module.user.online.UserOnlineExpMsgDao;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.service.jedis.RedisLock;
import avatar.util.checkParams.ErrorDealUtil;
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
            ErrorDealUtil.printError(e);
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
     * 更新在线信息非游戏玩家
     */
    public static void onlineMsgNoGaming(int userId, int productId) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                //查询在线信息
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(int i=0;i<list.size();i++){
                        UserOnlineMsgEntity entity = list.get(i);
                        if(entity.getIsGaming()==YesOrNoEnum.YES.getCode() && entity.getProductId()==productId){
                            if(i==0){
                                if(ParamsUtil.isConfirm(entity.getIsOnline())) {
                                    //首个，在线，更新非游戏中
                                    entity.setIsGaming(YesOrNoEnum.NO.getCode());//是否游戏中：否
                                    UserOnlineMsgDao.getInstance().update(0, entity);
                                }else{
                                    //直接删除
                                    UserOnlineMsgDao.getInstance().delete(entity);
                                }
                            }else{
                                //直接删除
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
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

    /**
     * 非街机结算游戏中
     */
    public static boolean isNoStreeSettlementGaming(int userId) {
        boolean isGaming = false;
        //查询在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                if(entity.getIsGaming()==YesOrNoEnum.YES.getCode()){
                    isGaming = true;
                    break;
                }
            }
        }
        return isGaming;
    }

    /**
     * 设备开始游戏
     */
    public static void startGameProduct(int userId, int productId, String serverIp, int serverPort) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                boolean onProductFlag = false;
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            onProductFlag = true;
                            entity.setIsGaming(YesOrNoEnum.YES.getCode());//是否游戏中：是
                            entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
                            UserOnlineMsgDao.getInstance().update(0, entity);
                            break;
                        }
                    }
                }
                if(!onProductFlag){
                    if(list.size()==1 && list.get(0).getProductId()==0){
                        //更新在线信息
                        UserOnlineMsgEntity entity = list.get(0);
                        entity.setProductId(productId);//设备ID
                        entity.setIsGaming(YesOrNoEnum.YES.getCode());//是否游戏中：是
                        UserOnlineMsgDao.getInstance().update(0, entity);
                    }else {
                        //添加在线信息
                        UserOnlineMsgEntity entity = initUserOnlineMsgEntity(userId, serverIp, serverPort, productId);
                        entity.setIsGaming(YesOrNoEnum.YES.getCode());//是否在线：是
                        UserOnlineMsgDao.getInstance().insert(entity);
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取玩家在线设备
     */
    public static int loadOnlineProduct(int userId){
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        return list.size()==0?0:list.get(0).getProductId();
    }

    /**
     * 进入设备
     */
    public static void joinProductMsg(int userId, int productId, String serverIp, int serverPort) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                boolean onProductFlag = false;
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            onProductFlag = true;
                            entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
                            UserOnlineMsgDao.getInstance().update(0, entity);
                            break;
                        }
                    }
                }
                if(!onProductFlag){
                    if(list.size()==1 && list.get(0).getProductId()==0){
                        //更新在线信息
                        UserOnlineMsgEntity entity = list.get(0);
                        entity.setProductId(productId);//设备ID
                        UserOnlineMsgDao.getInstance().update(0, entity);
                    }else {
                        //添加在线信息
                        UserOnlineMsgDao.getInstance().insert(initUserOnlineMsgEntity(userId, serverIp, serverPort, productId));
                    }
                }
                //删除其他非游戏中的信息
                List<UserOnlineMsgEntity> newList = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(newList.size()>0){
                    newList.forEach(entity->{
                        if(entity.getProductId()!=productId && entity.getIsGaming()!=YesOrNoEnum.YES.getCode()){
                            UserOnlineMsgDao.getInstance().delete(entity);
                        }
                    });
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 退出设备
     */
    public static void exitProductMsg(int userId, int productId) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId && entity.getIsGaming()!=YesOrNoEnum.YES.getCode()){
                            if(list.size()==1){
                                //更新设备信息为0
                                entity.setProductId(0);//设备ID
                                UserOnlineMsgDao.getInstance().update(productId, entity);
                            }else{
                                //直接删除
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取游戏中的设备
     */
    public static int loadGamingProduct(int userId) {
        int retPId = 0;
        //查询在线信息
        List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
        if(list.size()>0){
            for(UserOnlineMsgEntity entity : list){
                int productId = entity.getProductId();//设备ID
                if(entity.getIsGaming()==YesOrNoEnum.YES.getCode()){
                    retPId = productId;
                    break;
                }
            }
        }
        return retPId;
    }

    /**
     * 更新在线信息不在线
     */
    public static void onlineMsgNoOnline(int userId, int productId) {
        //获取玩家余额锁
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.USER_ONLINE_LOCK+"_"+userId,
                2000);
        try {
            if (lock.lock()) {
                //查询在线信息
                List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
                if(list.size()>0){
                    for(UserOnlineMsgEntity entity : list){
                        if(entity.getProductId()==productId){
                            if(entity.getIsGaming()==YesOrNoEnum.NO.getCode()){
                                //直接删除
                                UserOnlineMsgDao.getInstance().delete(entity);
                            }else{
                                //游戏中，改成不在线
                                entity.setIsOnline(YesOrNoEnum.NO.getCode());
                                UserOnlineMsgDao.getInstance().update(0, entity);
                            }
                        }
                        break;
                    }
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 更新玩家经验
     */
    public static void updateUserExp(int userId) {
        //查询在线经验
        UserOnlineExpMsg expMsg = UserOnlineExpMsgDao.getInstance().loadByMsg(userId);
        //查询玩家属性信息
        UserAttributeMsgEntity entity = UserAttributeMsgDao.getInstance().loadMsg(userId);
        if(expMsg!=null && entity!=null && expMsg.getExpNum()!=entity.getUserLevelExp()){
            entity.setUserLevelExp(expMsg.getExpNum());
            UserAttributeMsgDao.getInstance().update(entity);
        }
    }

    /**
     * 填充玩家在线经验信息
     */
    public static UserOnlineExpMsg initUserOnlineExpMsg(int userId) {
        UserOnlineExpMsg msg = new UserOnlineExpMsg();
        msg.setUserId(userId);//玩家ID
        msg.setCoinNum(0);//投入游戏币数
        msg.setExpNum(UserAttributeUtil.loadExpNum(userId));//经验数
        return msg;
    }

}
