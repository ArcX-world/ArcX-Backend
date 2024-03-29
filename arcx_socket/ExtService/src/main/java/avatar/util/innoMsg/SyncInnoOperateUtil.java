package avatar.util.innoMsg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 同步自研设备操作工具类
 */
public class SyncInnoOperateUtil {
    //本地缓存的socketmap
    public static ConcurrentMap<String, SyncInnoClient> webSocket = new ConcurrentHashMap<>();

    /**
     * 获取客户端
     */
    public static SyncInnoClient loadClient(String ip, int port, String linkMsg) {
        SyncInnoClient myClient = webSocket.get(linkMsg);
        if(myClient==null){
            //处理关闭重连
            SyncInnoDealUtil.socketClose(ip, port);
        }
        return myClient;
    }

    /**
     * 获取客户端
     */
    public static SyncInnoClient loadClient(String ip, int port) {
        String linkMsg = SyncInnoConnectUtil.linkMsg(ip, port);//链接信息
        SyncInnoClient myClient = webSocket.get(linkMsg);
        if(myClient==null || !myClient.isOpen()) {
            try {
                if (SyncInnoConnectUtil.isOutTimeLink(linkMsg)) {
                    //重新生成连接
                    myClient = new SyncInnoClient("ws://" + ip + ":" + port + "/websocket");
                    //开启连接
                    myClient.connect();
                    //设置缓存
                    webSocket.put(linkMsg, myClient);
                }
            } catch (Exception e) {
                myClient = null;
            }
        }
        return myClient;
    }

}
