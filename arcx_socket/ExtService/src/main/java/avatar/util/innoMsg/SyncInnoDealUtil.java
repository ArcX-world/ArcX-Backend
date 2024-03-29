package avatar.util.innoMsg;

import avatar.global.Config;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SyncInnoHeartTimeDao;
import avatar.module.product.innoMsg.SyncInnoReconnectDao;
import avatar.service.jedis.RedisLock;
import avatar.task.innoMsg.ReconnectInnoSocketTask;
import avatar.task.innoMsg.SyncInnoHeartTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * 自研设备处理工具类
 */
public class SyncInnoDealUtil {
    /**
     * socket连接成功处理
     */
    public static void socketOpen(String host, int port) {
        //不同版本websocket
        LogUtil.getLogger().info("IP：{}的自研设备服务器连接成功了，开始发送心跳------", host);
        long startHeartTime = TimeUtil.getNowTime();//开启心跳时间
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        //设置心跳缓存
        SyncInnoHeartTimeDao.getInstance().setCache(linkMsg, startHeartTime);
        SchedulerSample.delayed(2000, new SyncInnoHeartTask(host, port, startHeartTime));
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        //socket关闭处理
        socketCloseDeal(fromKey, linkMsg);
    }

    /**
     * socket关闭处理
     */
    public static void socketClose(String host, int port) {
        LogUtil.getLogger().info("IP：{}，端口{}的自研设备服务器连接关闭了------", host, port);
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = SyncInnoConnectUtil.linkMsg(host, port);//to key
        //删除本地缓存
        SyncInnoOperateUtil.webSocket.remove(toKey);
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SYNC_INNO_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                int status = SyncInnoReconnectDao.getInstance().loadMsg(fromKey, toKey);
                if(status== YesOrNoEnum.NO.getCode()){
                    SyncInnoReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.YES.getCode());
                    //3秒后重新连
                    SchedulerSample.delayed(3000, new ReconnectInnoSocketTask(host, port, fromKey, toKey));
                }else{
                    LogUtil.getLogger().info("自研设备socket关闭重连的时候，ip：{}，端口：{}已经处于重连状态，不处理---",
                            host, port);
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * socket连接报错处理
     */
    public static void socketError(String host, int port) {
        LogUtil.getLogger().info("IP：{}，端口{}的自研设备服务器连接报错了------", host, port);
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        int uId = SyncInnoConnectUtil.loadHostId(host, port+"");
        String toKey = host+port+uId;//to key
        //删除本地缓存
        SyncInnoOperateUtil.webSocket.remove(toKey);
        //socket关闭处理
        socketCloseDeal(fromKey, toKey);
    }

    /**
     * socket关闭处理
     */
    public static void socketCloseDeal(String fromKey, String toKey) {
        //设置重连状态缓存
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.SYNC_INNO_RECONNECT_LOCK,
                2000);
        try {
            if (lock.lock()) {
                SyncInnoReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.NO.getCode());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }
}
