package avatar.global.enumMsg.basic.errrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：普通设备通用错误码
 */
public enum NormalProductClientCode {
    FAIL(0,"失败"),
    SUCCESS(1,"成功"),
    PRODUCT_OCCUPY(1011,"设备占用中"),
    PRODUCT_GAMING_USER_NOT_FIT(1012,"你已离开设备"),
    PRODUCT_EXCEPTION(1015,"设备异常"),
    TWICE_OPERATE(1018,"重复操作"),
    ;


    private int code;//错误码

    private String name;//中文描述

    NormalProductClientCode(int code, String name){
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
        for (ClientCode clientCode : ClientCode.values()) {
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
