package avatar.util.normalProduct;

import avatar.util.checkParams.ErrorDealUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 普通设备内部操作工具类
 */
public class InnerNormalProductOperateUtil {
    //本地缓存的socketmap
    public static ConcurrentMap<String, InnerNormalProductClient> webSocket = new ConcurrentHashMap<>();

    /**
     * 获取客户端
     * @param ip 服务端IP
     * @param port 服务端端口
     * @param linkMsg 连接信息
     */
    public static InnerNormalProductClient loadClient(String ip, int port, String linkMsg) {
        InnerNormalProductClient myClient = webSocket.get(linkMsg);
        if(myClient==null){
            //处理关闭重连
            InnerNormalProductDealUtil.socketClose(ip, port);
        }
        return myClient;
    }

    /**
     * 获取客户端
     * @param ip 服务端IP
     * @param port 服务端端口
     */
    public static InnerNormalProductClient loadClient(String ip, int port) {
        int uId = InnerNormalProductConnectUtil.loadUid(ip, port);
        String linkMsg = ip+port+uId;//连接信息
        InnerNormalProductClient myClient = webSocket.get(linkMsg);
        if(myClient==null || !myClient.isOpen()) {
            try {
                if (InnerNormalProductConnectUtil.isOutTimeLink(linkMsg)) {
                    //重新生成连接
                    myClient = new InnerNormalProductClient("ws://" + ip + ":" + port + "/websocket");
                    //开启连接
                    myClient.connect();
                    //设置缓存
                    webSocket.put(linkMsg, myClient);
                }
            } catch (Exception e) {
                ErrorDealUtil.printError(e);
                myClient = null;
            }
        }
        return myClient;
    }
}
