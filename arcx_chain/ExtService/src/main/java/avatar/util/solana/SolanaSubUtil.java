package avatar.util.solana;

import avatar.util.LogUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.StrUtil;

import java.util.Map;

/**
 * solana订阅工具类
 */
public class SolanaSubUtil {
    /**
     * logsNotification订阅
     */
    public static void logsNotification(String jsonStr) {
        String signature = "";
        if(jsonStr.contains("signature")){
            Map<String, Object> paramsMap = (Map<String, Object>) (JsonUtil.strToMap(jsonStr)).get("params");
            if(paramsMap.containsKey("result")){
                Map<String, Object> resultMap = (Map<String, Object>) paramsMap.get("result");
                if(resultMap.containsKey("value")){
                    Map<String, Object> valueMap = (Map<String, Object>) resultMap.get("value");
                    if(valueMap.containsKey("signature")){
                        signature = valueMap.get("signature").toString();
                    }
                }
            }
        }
//        LogUtil.getLogger().info("接收到的logsNotification订阅解析出来的签名内容{}---------", signature);
        if(!StrUtil.checkEmpty(signature)){
            //查询交易
            if(!SolanaRequestUtil.repeatDescribe(signature)) {
                SolanaRequestUtil.loadTransaction(signature);
            }
        }
    }
}
