package avatar.global.enumMsg.basic.errrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：错误码
 */
public enum ClientCode {
    FAIL(0,"失败", "fail"),
    SUCCESS(1,"成功", "success"),
    LIMIT(1000,"limit", "limit"),
    ACCOUNT_DISABLED(1001,"账号异常", "Network error"),//账号异常
    SYSTEM_MAINTAIN(1002,"系统维护中", "System Maintaining"),
    PARAMS_ERROR(1003,"参数错误，请核对", "Parameter error, please check"),
    VERIFY_SIGN_ERROR(1004,"验证签名失败", "Verification signature failed"),
    USER_NO_EXIST(1005,"玩家信息不存在", "Player information does not exist"),
    ACCESS_TOKEN_OUT_TIME(1006,"调用凭证过期", "access_token out time"),
    ACCESS_TOKEN_ERROR(1007,"请更新APP", "access token error"),
    REFRESH_TOKEN_OUT_TIME(1008,"刷新凭证过期", "refresh token out time"),
    REFRESH_TOKEN_NOT_FIT(1009,"刷新凭证不匹配", "refresh token not fit"),
    PRODUCT_EXCEPTION(1010,"机器故障", "machine fault"),
    PRODUCT_OCCUPY(1011,"设备占用中", "Equipment occupied"),
    BALANCE_NO_ENOUGH(1012,"余额不足", "Balance is insufficient"),
    PRODUCT_GAMING_USER_NOT_FIT(1013,"你已离开设备", "You have left the device"),//设备玩家不匹配
    TWICE_OPERATE(1014,"重复操作", "Repeat operation"),
    MULTI_LOCK(1015,"请稍后", "Please wait a moment"),//倍率冷却限制
    AWARD_LOCK(1016,"中奖锁定中","Winning lock in"),
    GAMING_FORBID(1017, "您正在游戏中，请等待游戏结束","You are in the game, please wait for the game to end"),
    SYSTEM_ERROR(1018,"系统错误", "system error"),
    PRODUCT_NO_EXIST(1019,"设备不存在", "The device does not exist"),
    PRODUCT_TYPE_ERROR(1020,"设备类型错误", "Device type error"),
    ;


    private int code;//错误码

    private String name;//中文描述

    private String english;//英文描述

    ClientCode(int code, String name, String english){
        this.code = code;
        this.name = name;
        this.english = english;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    public String getEnglish(){return english;}

    /**
     * 英文描述
     */
    public static Map<Integer, String> toEnglishMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ClientCode clientCode : ClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getEnglish());
        }
        return map;
    }

    /**
     * 根据错误码获取错误信息
     */
    public static String getErrorMsgByStatus(int status){
        return toEnglishMap().get(status);
    }

}
