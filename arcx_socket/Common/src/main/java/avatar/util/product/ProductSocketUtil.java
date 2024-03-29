package avatar.util.product;

import avatar.global.lockMsg.LockMsg;
import avatar.module.product.gaming.UserJoinProductDao;
import avatar.net.session.Session;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 设备socket信息
 */
public class ProductSocketUtil {
    //设备session对象
    public static ConcurrentMap<Integer, List<Session>> sessionMap = new ConcurrentHashMap<>();

    /**
     * 是否设备在线
     */
    public static boolean isSessionOnline(int userId, int productId) {
        String accessToken = UserUtil.loadAccessToken(userId);//玩家通行证
        boolean flag = false;//session在线标志
        if(!StrUtil.checkEmpty(accessToken)) {
            List<Session> sessionList = ProductSocketUtil.sessionMap.get(productId);
            if (sessionList.size() > 0) {
                for (Session session : sessionList) {
                    if (session != null && accessToken.equals(session.getAccessToken())) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 处理设备在线session
     */
    public static List<Session> dealOnlineSession(int productId) {
        List<Session> retSessionList = new ArrayList<>();
        List<Session> sessionList = ProductSocketUtil.sessionMap.get(productId);
        if(sessionList!=null && sessionList.size()>0){
            sessionList.forEach(session -> {
                if(session!=null){
                    retSessionList.add(session);
                }
            });
            if(sessionList.size()!=retSessionList.size()){
                ProductSocketUtil.sessionMap.put(productId, retSessionList);
            }
        }
        return retSessionList;
    }

    /**
     * 处理下线设备socket
     */
    public static void dealOffLineSession(int userId) {
        String accessToken = UserUtil.loadAccessToken(userId);//玩家通行证
        List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
        if(list.size()>0){
            //删除对应的设备信息
            list.forEach(productId->{
                LogUtil.getLogger().error("玩家{}断socket删除设备{}的在线session信息--------", userId, productId);
                RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                        2000);
                try {
                    if (lock.lock()) {
                        List<Session> sessionList = dealOnlineSession(productId);
                        if(sessionList.size()>0) {
                            List<Session> newSessionList = new ArrayList<>(sessionList);
                            newSessionList.removeIf(ses -> (ses.getAccessToken().equals(accessToken)));
                            sessionMap.put(productId, newSessionList);
                            //进入设备信息处理
                            joinProductMsgDeal(userId, productId, false);
                        }
                    }
                }catch (Exception e){
                    ErrorDealUtil.printError(e);
                }finally {
                    lock.unlock();
                }
            });
        }
    }

    /**
     * 进入设备信息缓存处理
     */
    private static void joinProductMsgDeal(int userId, int productId, boolean joinRoomFlag) {
        if(userId>0){
            List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
            if(joinRoomFlag){
                //进入设备
                if(!list.contains(productId)){
                    list.add(productId);
                    UserJoinProductDao.getInstance().setCache(userId, list);
                }
            }else{
                //退出设备
                if(list.contains(productId)){
                    List<Integer> newList = new ArrayList<>(list);
                    newList.removeIf(pId -> (pId==productId));
                    UserJoinProductDao.getInstance().setCache(userId, newList);
                }
            }
        }
    }

    /**
     * 处理下线设备socket
     * 异常处理
     */
    public static void dealOffLineSession(int userId, int pId) {
        String accessToken = UserUtil.loadAccessToken(userId);//玩家通行证
        List<Integer> list = UserJoinProductDao.getInstance().loadByMsg(userId);
        if(list.size()>0){
            //删除对应的设备信息
            list.forEach(productId->{
                if(productId==pId){
                    LogUtil.getLogger().error("玩家{}异常断socket删除设备{}的在线session信息--------", userId, productId);
                    RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                            2000);
                    try {
                        if (lock.lock()) {
                            List<Session> sessionList = dealOnlineSession(productId);
                            if(sessionList.size()>0) {
                                List<Session> newSessionList = new ArrayList<>(sessionList);
                                newSessionList.removeIf(ses -> (ses.getAccessToken().equals(accessToken)));
                                sessionMap.put(productId, newSessionList);
                                //进入设备信息处理
                                joinProductMsgDeal(userId, productId, false);
                            }
                        }
                    }catch (Exception e){
                        ErrorDealUtil.printError(e);
                    }finally {
                        lock.unlock();
                    }
                }
            });
        }
    }

    /**
     * 加入设备
     */
    public static void joinProduct(int productId, Session session) {
        List<Session> sessionList = dealOnlineSession(productId);
        if(!sessionList.contains(session)) {
            sessionList.add(session);
            sessionMap.put(productId, sessionList);
            int userId = UserUtil.loadUserIdByToken(session.getAccessToken());//玩家ID
            //进入设备信息处理
            joinProductMsgDeal(userId, productId, true);
        }
        LogUtil.getLogger().info("进入设备{}后的session数量{}------", productId, sessionMap.get(productId).size());
    }

    /**
     * 退出设备
     */
    public static void exitProduct(int productId, Session session) {
        List<Session> sessionList = dealOnlineSession(productId);
        if(sessionList.size()>0){
            List<Session> newSessionList = new ArrayList<>(sessionList);
            newSessionList.removeIf(ses -> (ses==session || ses.equals(session)));
            sessionMap.put(productId, newSessionList);
            int userId = UserUtil.loadUserIdByToken(session.getAccessToken());//玩家ID
            //进入设备信息处理
            joinProductMsgDeal(userId, productId, false);
        }
        LogUtil.getLogger().info("退出设备{}后的session数量{}------", productId, sessionMap.get(productId)==null?0:
                sessionMap.get(productId).size());
    }

    /**
     * 处理进入设备
     */
    public static void dealJoinProduct(int productId, Session session) {
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.PRODUCT_SESSION_LOCK+"_"+productId,
                2000);
        try {
            if (lock.lock()) {
                joinProduct(productId, session);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
