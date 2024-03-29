package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：错误码
 */
public enum ClientCode {
    FAIL(0,"失败"),
    SUCCESS(1,"成功"),
    LIMIT(1000,"limit"),
    ACCOUNT_DISABLED(1001,"账号异常"),//账号异常
    SYSTEM_MAINTAIN(1002,"系统维护中"),
    PARAMS_ERROR(1003,"参数错误，请核对"),
    VERIFY_SIGN_ERROR(1004,"验证签名失败"),
    USER_NO_EXIST(1005,"玩家信息不存在"),
    ACCESS_TOKEN_OUT_TIME(1006,"调用凭证过期"),
    ACCESS_TOKEN_ERROR(1007,"请更新APP"),
    REFRESH_TOKEN_OUT_TIME(1008,"刷新凭证过期"),
    REFRESH_TOKEN_NOT_FIT(1009,"刷新凭证不匹配"),
    ;


    private int code;//错误码

    private String name;//中文描述

    ClientCode(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**
     * 描述
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (ClientCode clientCode : ClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getName());
        }
        return map;
    }

    /**
     * 根据错误码获取错误信息
     */
    public static String getErrorMsgByStatus(int status){
        return toMap().get(status);
    }

}
