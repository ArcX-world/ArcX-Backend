package avatar.util.basic.encode;

import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.util.LogUtil;
import avatar.util.checkParams.ErrorDealUtil;
import avatar.util.system.JsonUtil;
import avatar.util.system.MD5Util;
import avatar.util.system.ParamsUtil;
import avatar.util.system.StrUtil;
import avatar.util.user.UserUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * websocket加密检测工具类
 */
public class WebsocketEncodeUtil {
    /**
     * 加密检测
     * MD5(timestamp+code+key+code)
     */
    public static int checkEncode(String accessToken, boolean checkTokenFlag, JSONObject jsonObject) {
        int status = ClientCode.SUCCESS.getCode();//成功
        int userId = 0;//玩家ID
        try{
            if(checkTokenFlag || !UserUtil.isTourist(accessToken)) {
                status = UserUtil.checkAccessToken(accessToken);//检测玩家通行证
            }
            if(ParamsUtil.isConfirm(status)) {
                String arcxPas = jsonObject.getString("arcxPsd");//加密后的结果
                long tsmp = jsonObject.getLongValue("tsmp");//时间戳
                String verifyStr = jsonObject.getLongValue("tsmp") + "+";//检测加密的字符串
                int randomNum = jsonObject.getIntValue("rdAmt");//随机数
                if (!StrUtil.checkEmpty(accessToken) && !UserUtil.isTourist(accessToken)) {
                    userId = UserUtil.loadUserIdByToken(accessToken);
                }
                String code = tsmp+"";
                if ((randomNum+"").length()!=13) {
                    verifyStr += firstCode(code, randomNum);//第一个字符
                    verifyStr += ("+" + accessToken);
                    verifyStr += "+";
                    verifyStr += secondCode(code);//第二个字符
                    if (!MD5Util.MD5(verifyStr).equals(arcxPas)) {
                        status = ClientCode.VERIFY_SIGN_ERROR.getCode();//验签失败
                    }
                }else{
                    LogUtil.getLogger().error("websocket验签传的时间戳不是13位------");
                    status = ClientCode.VERIFY_SIGN_ERROR.getCode();//验签失败
                }
            }
        }catch (Exception e){
            ErrorDealUtil.printError(e);
            status = ClientCode.VERIFY_SIGN_ERROR.getCode();//验签失败
        }
        if(ParamsUtil.isVerifyError(status)){
            LogUtil.getLogger().error("玩家{}发送的信息{}验签失败**********", userId, JsonUtil.mapToJson(jsonObject));
        }
        return status;
    }

    /**
     * 第一个加密code
     * 设备唯一ID，前10位包含的数字进行异或，不包含数字则用数字1去异或
     * @param mac 设备唯一ID
     */
    private static String firstCode(String mac, int randomNum) {
        int totalNum = Integer.parseInt(String.valueOf(mac.charAt(1)))+
                Integer.parseInt(String.valueOf(mac.charAt(3)))+
                Integer.parseInt(String.valueOf(mac.charAt(5)))+
                Integer.parseInt(String.valueOf(mac.charAt(7)))+
                Integer.parseInt(String.valueOf(mac.charAt(8)));
        return totalNum*randomNum+"";
    }

    /**
     * 第二个加密code
     * 设备唯一ID的第2~4位
     * @param mac 设备唯一ID
     */
    private static String secondCode(String mac) {
        return mac.substring(1, 4);
    }
}
