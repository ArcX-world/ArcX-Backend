package avatar.global.enumMsg.basic.recharge;

import java.util.*;

/**
 * 枚举：支付类型
 */
public enum PayTypeEnum {
    USDT(1,"USDT支付"),
    ;

    private int code;
    private String name;//中文名称

    PayTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**
     * 获取所有枚举
     */
    public static List<PayTypeEnum> loadAll(){
        PayTypeEnum[] enumArr = PayTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PayTypeEnum pay : PayTypeEnum.values()) {
            map.put(pay.getCode(), pay.getName());
        }
        return map;
    }

    /**
     * 根据支付码获取支付信息
     */
    public static String getPayMsgByType(int payType){
        return toMap().get(payType);
    }

}
