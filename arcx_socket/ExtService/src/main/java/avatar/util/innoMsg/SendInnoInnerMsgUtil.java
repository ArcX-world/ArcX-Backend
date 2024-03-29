package avatar.util.innoMsg;

import avatar.util.product.ProductUtil;
import avatar.util.system.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送自研设备内部信息工具类
 */
public class SendInnoInnerMsgUtil {
    /**
     * 模拟客户端发送信息
     */
    public static void sendClientMsg(SyncInnoClient client, int cmd, int hostId,
                                     Map<Object, Object> paramsMap) {
        //拼接信息
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("cmd", cmd);
        msgMap.put("userId", hostId);
        msgMap.put("param", paramsMap);
        //加密处理
        InnerEnCodeUtil.objectEncodeDeal(paramsMap);
        String jsonStr = JsonUtil.mapToJson(msgMap);
        //推送信息
        client.send(jsonStr);
    }

    /**
     * 推送自研设备信息
     */
    public static void sendClientMsg(int productId, int cmd, Map<Object, Object> paramsMap) {
        String ip = ProductUtil.productIp(productId);//设备IP
        int port = ProductUtil.productSocketPort(productId);//设备端口
        String linkMsg = SyncInnoConnectUtil.linkMsg(ip, port);//链接信息
        //发送心跳
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(ip, port, linkMsg);
        if (client != null && client.isOpen()) {
            int hostId = SyncInnoConnectUtil.loadHostId(client.getURI().getHost(), client.getURI().getPort()+"");
            if(hostId>0) {
                //拼接信息
                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("cmd", cmd);
                msgMap.put("userId", hostId);
                msgMap.put("param", paramsMap);
                //加密处理
                InnerEnCodeUtil.objectEncodeDeal(paramsMap);
                String jsonStr = JsonUtil.mapToJson(msgMap);
                //推送信息
                client.send(jsonStr);
            }
        }
    }

}
