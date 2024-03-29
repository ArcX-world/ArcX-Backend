package avatar.util.normalProduct;

import avatar.global.Config;
import avatar.global.enumMsg.system.YesOrNoEnum;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.normalProduct.InnerNormalProductHeartTimeDao;
import avatar.module.product.normalProduct.InnerNormalProductReconnectDao;
import avatar.service.jedis.RedisLock;
import avatar.task.normalProduct.InnerNormalProductHeartTask;
import avatar.task.normalProduct.ReconnectInnerNormalProductSocketTask;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;
import avatar.util.trigger.SchedulerSample;

/**
 * 普通设备内部连接处理工具类
 */
public class InnerNormalProductDealUtil {
    /**
     * socket连接成功处理
     */
    public static void socketOpen(String host, int port) {
        LogUtil.getLogger().info("IP：{}，端口：{}的普通设备服务器连接成功了，开始发送心跳------", host, port);
        long startHeartTime = TimeUtil.getNowTime();//开启心跳时间
        int uId = InnerNormalProductConnectUtil.loadUid(host, port);//socket唯一ID
        String linkMsg = host+port+uId;
        //设置心跳缓存
        InnerNormalProductHeartTimeDao.getInstance().setCache(linkMsg, startHeartTime);
        SchedulerSample.delayed(2000, new InnerNormalProductHeartTask(host, port, startHeartTime));
        String fromKey = Config.getInstance().getLocalAddr() + Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+uId;//to key
        //socket关闭处理
        socketCloseDeal(fromKey, toKey);
    }

    /**
     * socket关闭处理
     */
    public static void socketCloseDeal(String fromKey, String toKey) {
        //设置重连状态缓存
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNER_NORMAL_PRODUCT_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                InnerNormalProductReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.NO.getCode());
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * socket关闭处理
     */
    public static void socketClose(String host, int port) {
        LogUtil.getLogger().info("IP：{}，端口：{}的普通设备服务器连接连接关闭了------", host, port);
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+ InnerNormalProductConnectUtil.loadUid(host, port);//to key
        //删除本地缓存
        InnerNormalProductOperateUtil.webSocket.remove(toKey);
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.INNER_NORMAL_PRODUCT_RECONNECT_LOCK+toKey,
                2000);
        try {
            if (lock.lock()) {
                int status = InnerNormalProductReconnectDao.getInstance().loadMsg(fromKey, toKey);
                if(status== YesOrNoEnum.NO.getCode()){
                    InnerNormalProductReconnectDao.getInstance().setCache(fromKey, toKey, YesOrNoEnum.YES.getCode());
                    //3秒后重新连
                    SchedulerSample.delayed(3000, new ReconnectInnerNormalProductSocketTask(host, port));
                }else{
                    LogUtil.getLogger().info("普通设备服务器链接socket关闭重连的时候，ip：{}，端口：{}已经处于重连状态，不处理---",
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
        LogUtil.getLogger().info("IP：{}，端口：{}的普通设备服务器连接连接报错了------", host, port);
        String fromKey = Config.getInstance().getLocalAddr()+ Config.getInstance().getWebSocketPort();//from key
        String toKey = host+port+ InnerNormalProductConnectUtil.loadUid(host, port);//to key
        //删除本地缓存
        InnerNormalProductOperateUtil.webSocket.remove(toKey);
        //socket关闭处理
        socketCloseDeal(fromKey, toKey);
    }
}
