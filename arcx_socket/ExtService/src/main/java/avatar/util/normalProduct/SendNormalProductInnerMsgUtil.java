package avatar.util.normalProduct;

import avatar.util.innoMsg.InnerEnCodeUtil;
import avatar.util.system.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送普通设备内部信息工具类
 */
public class SendNormalProductInnerMsgUtil {
    /**
     * 模拟客户端发送信息
     */
    public static void sendClientMsg(InnerNormalProductClient client, int cmd, int hostId, Map<String, Object> paramsMap) {
        //拼接信息
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("cmd", cmd);
        msgMap.put("userId", hostId);
        msgMap.put("param", paramsMap);
        //加密处理
        InnerEnCodeUtil.encodeDeal(paramsMap);
        String jsonStr = JsonUtil.mapToJson(msgMap);
        //推送信息
        client.send(jsonStr);
    }

}
