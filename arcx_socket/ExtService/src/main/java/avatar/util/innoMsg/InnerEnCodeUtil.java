package avatar.util.innoMsg;

import avatar.global.code.basicConfig.ConfigMsg;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.TimeUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 内部加密处理工具类
 */
public class InnerEnCodeUtil {
    /**
     * 加密处理
     */
    public static void encodeDeal(Map<String, Object> paramsMap) {
        long sysTime = TimeUtil.getNowTime();//当前时间
        try {
            String encodeStr = MD5Util.MD5(sysTime + "+" + loadKey(sysTime) + "+" + ConfigMsg.sysPlatform);
            paramsMap.put("sysTime", sysTime);//系统时间
            paramsMap.put("encodeStr", encodeStr);//加密字符串
            paramsMap.put("platform", ConfigMsg.sysPlatform);//平台类型
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

    /**
     * obj加密处理
     */
    public static void objectEncodeDeal(Map<Object, Object> paramsMap) {
        long sysTime = TimeUtil.getNowTime();//当前时间
        try {
            String encodeStr = MD5Util.MD5(sysTime + "+" + loadKey(sysTime) + "+" + ConfigMsg.sysPlatform);
            paramsMap.put("sysTime", sysTime);//系统时间
            paramsMap.put("encodeStr", encodeStr);//加密字符串
            paramsMap.put("platform", ConfigMsg.sysPlatform);//平台类型
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
    }

    /**
     * 加密检测
     */
    public static boolean checkEncode(Map<String, Object> jsonMap) {
        boolean flag = false;
        try {
            if (jsonMap != null && jsonMap.containsKey("sysTime") && jsonMap.containsKey("encodeStr") &&
                    jsonMap.containsKey("platform")) {
                long sysTime = Long.parseLong(jsonMap.get("sysTime").toString());//系统时间
                String encodeStr = (String) jsonMap.get("encodeStr");//加密字符串
                String platform = (String) jsonMap.get("platform");//平台类型
                String checkEncodeStr = MD5Util.MD5(sysTime + "+" + loadKey(sysTime) + "+" + platform);
                flag = encodeStr.equals(checkEncodeStr);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**
     * 加密检测
     */
    public static boolean checkEncode(JSONObject jsonObject) {
        boolean flag = false;
        try {
            if (jsonObject!=null && jsonObject.containsKey("sysTime") && jsonObject.containsKey("encodeStr") &&
                    jsonObject.containsKey("platform")) {
                long sysTime = Long.parseLong(jsonObject.get("sysTime").toString());//系统时间
                String encodeStr = (String) jsonObject.get("encodeStr");//加密字符串
                String platform = (String) jsonObject.get("platform");//平台类型
                String checkEncodeStr = MD5Util.MD5(sysTime + "+" + loadKey(sysTime) + "+" + platform);
                flag = encodeStr.equalsIgnoreCase(checkEncodeStr);
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return flag;
    }

    /**
     * 获取key
     */
    private static String loadKey(long sysTime) {
        String time = TimeUtil.longToSimpleDay(sysTime);
        return "DaylongPushCoin"+time;
    }
}
