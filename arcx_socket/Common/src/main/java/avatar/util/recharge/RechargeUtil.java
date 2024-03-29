package avatar.util.recharge;

import avatar.global.enumMsg.basic.recharge.PayStatusEnum;

/**
 * 充值工具类
 */
public class RechargeUtil {
    /**
     * 获取微信支付的类型
     */
    public static String loadSuccessPayStr(){
        //包含已支付，手动未回调，手动回调
        String payTypeStr = "";
        payTypeStr += (PayStatusEnum.ALREADY_PAY.getCode()+",");//已支付
        payTypeStr += (PayStatusEnum.HAND_NO_CALL.getCode()+",");//手动支付未回调
        payTypeStr += (PayStatusEnum.HAND_AFTER_CALL.getCode());//手动支付已回调
        return payTypeStr;
    }

}
