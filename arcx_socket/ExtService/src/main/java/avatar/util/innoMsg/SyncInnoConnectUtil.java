package avatar.util.innoMsg;

import avatar.entity.product.innoMsg.SyncInnoProductIpEntity;
import avatar.global.Config;
import avatar.global.lockMsg.LockMsg;
import avatar.module.product.innoMsg.SyncInnoProductIpDao;
import avatar.module.product.socket.ConnectSocketLinkDao;
import avatar.service.jedis.RedisLock;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.TimeUtil;

import java.util.List;

/**
 * 自研设备交互工具类
 */
public class SyncInnoConnectUtil {
    /**
     * 初始化自研设备服务器连接
     */
    public static void connectInnoProductServer() {
        //查询服务器信息
        List<SyncInnoProductIpEntity> list = SyncInnoProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            list.forEach(entity->{
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort().equals(Config.getInstance().getWebSocketPort()+"")){
                    SyncInnoOperateUtil.loadClient(entity.getToIp(), Integer.parseInt(entity.getToPort()));
                }
            });
        }else{
            LogUtil.getLogger().info("初始化自研设备服务器的websocket的时候，查询不到服务器信息");
        }
    }

    /**
     * 获取链接信息
     */
    public static String linkMsg(String ip, int port){
        return ip+port+loadHostId(ip, port+"");
    }

    /**
     * 根据IP获取对应的ID
     */
    public static int loadHostId(String ip, String port) {
        int hostId = 0;
        List<SyncInnoProductIpEntity> list = SyncInnoProductIpDao.getInstance().loadAll();
        if(list!=null && list.size()>0){
            for(SyncInnoProductIpEntity entity : list){
                if(entity.getFromIp().equals(Config.getInstance().getLocalAddr()) &&
                        entity.getFromPort().equals(Config.getInstance().getWebSocketPort()+"") &&
                        entity.getToIp().equals(ip) && entity.getToPort().equals(port)){
                    hostId = entity.getUserId();
                    break;
                }
            }
        }
        return hostId;
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
}
