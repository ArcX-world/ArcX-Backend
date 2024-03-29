package avatar.global.enumMsg.product.info;

/**
 * 枚举：游戏状态
 */
public enum GamingStateEnum {
    NO_PROPLE(1,"无人上机环节"),
    GAMING(2,"游戏环节"),
    CHOOSE_LIFE_DEATH(3,"选择生死环节"),
    RECHARGE_CONTINUE(4,"充值续命环节"),
    ;

    private int code;
    private String name;

    GamingStateEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }
}
