package avatar.util.normalProduct;

import avatar.entity.product.normalProduct.InnerNormalProductIpEntity;
import avatar.global.Config;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.normalProduct.InnerNormalProductIpDao;
import avatar.module.product.socket.ConnectSocketLinkDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 普通设备内部连接交互工具类
 */
public class InnerNormalProductConnectUtil {
    /**
     * 初始化设备服务端连接
     */
    public static void connectProductServer() {
        List<InnerNormalProductIpEntity> list = InnerNormalProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort()== Config.getInstance().getWebSocketPort()){
                    InnerNormalProductOperateUtil.loadClient(entity.getToIp(), entity.getToPort());
                }
            });
        }else{
            LogUtil.getLogger().info("初始化普通设备服务端的websocket的时候，查询不到设备服务器信息");
        }
    }

    /**
     * 是否超时链接
     */
    public static boolean isOutTimeLink(String linkMsg) {
        boolean flag = false;
        RedisLock lock = new RedisLock(RedisLock.loadCache(), LockMsg.CONNECT_SOCKET_LINK_LOCK+linkMsg,
                2000);
        try {
            if (lock.lock()) {
                long time = ConnectSocketLinkDao.getInstance().loadByLinkMsg(linkMsg);
                if((TimeUtil.getNowTime()-time)>2000){
                    //连接超时时间5秒
                    flag = true;
                    //重置连接时间
                    ConnectSocketLinkDao.getInstance().setCache(linkMsg, TimeUtil.getNowTime());
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
     * 获取唯一ID
     */
    public static int loadUid(String host, int port) {
        int hostId = 0;
        List<InnerNormalProductIpEntity> list = InnerNormalProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            for(InnerNormalProductIpEntity entity : list){
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort()== Config.getInstance().getWebSocketPort() &&
                        entity.getToIp().equals(host) && entity.getToPort()==port){
                    hostId = entity.getUserId();
                    break;
                }
            }
        }
        return hostId;
    }

}
