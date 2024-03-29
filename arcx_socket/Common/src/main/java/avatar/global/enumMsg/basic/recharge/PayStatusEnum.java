package avatar.global.enumMsg.basic.recharge;

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
}
