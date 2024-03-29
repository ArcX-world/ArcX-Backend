package avatar.global.enumMsg.basic.recharge;

import java.util.*;

/**
 * 支付状态
 */
public enum PayStatusEnum {
    NO_PAY(0, "未支付"),
    ALREADY_PAY(1,"已支付"),
    FAILED_PAY(2, "支付失败"),
    HAND_NO_CALL(3,"手动支付未回调"),
    HAND_AFTER_CALL(4,"手动支付已回调"),
    REFUND(5,"已退款"),;

    private int code;

    private String name;

    PayStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    /**
     * 获取所有枚举
     */
    public static List<PayStatusEnum> loadAll(){
        PayStatusEnum[] enumArr = PayStatusEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PayStatusEnum awardTypeEnum : PayStatusEnum.values()) {
            map.put(awardTypeEnum.getCode(), awardTypeEnum.getName());
        }
        return map;
    }

    /**
     * 根据code获取名称
     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }
}
