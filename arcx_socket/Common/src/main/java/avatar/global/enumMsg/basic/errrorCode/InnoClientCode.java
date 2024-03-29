package avatar.global.enumMsg.basic.errrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：自研设备错误码
 */
public enum InnoClientCode {
    NULL(-1,"空"),
    FAIL(0,"失败"),
    SUCCESS(1,"成功"),
    VERIFY_SIGN_ERROR(1001,"验证签名失败"),
    PRODUCT_EXCEPTION(1002,"设备异常"),
    PRODUCT_OCCUPY(1003,"设备占用中"),
    ;


    private int code;//错误码

    private String name;//中文描述

    InnoClientCode(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}


    /**
     * 中文描述
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (InnoClientCode clientCode : InnoClientCode.values()) {
            map.put(clientCode.getCode(), clientCode.getName());
        }
        return map;
    }

    /**
     * 根据错误码获取中文错误信息
     */
    public static String getErrorMsgByStatus(int status){
        return toMap().get(status);
    }

}
