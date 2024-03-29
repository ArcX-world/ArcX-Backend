package avatar.global.enumMsg.product.info;

/**
 * 设备操作类型枚举
 */
public enum ProductOperationEnum {
    START_GAME(0,"开始游戏"),
    FORWARD(1,"向前"),
    BACKWARDS(2,"向后"),
    LEFT(3,"向左"),
    RIGHT(4,"向右"),
    STOP(5,"停止天车"),
    CATCH(6,"下爪子抓娃娃"),
    RESET(7,"复位并获取结果"),
    PUSH_COIN(8,"投币"),
    OFF_LINE(9,"退出设备"),
    STRONG_CATCH(10,"强爪"),
    CATCH_RESULT(11,"抓取结果"),
//    DOWN_PRODUCT(12,"下机观看"),
    ROCK(13,"摇摆"),
    GET_COIN(14,"获得币"),
    DOWN_COIN(15,"掉币"),
    SHOOT(16,"发射"),
    CHANGE_GUN(17,"换炮"),
//    SETTLEMENT(18,"只进行结算"),
    CHECK_PRODUCT(19,"检测设备"),
    AUTO_SHOOT(20,"自动发炮"),
    CANCEL_AUTO_SHOOT(21,"取消自动发炮"),
    ERROR_CHECK(22,"故障检测"),
//    CHARTER_SETTLEMENT(23,"包机结算"),
    AUTO_PUSH_COIN(24,"自动投币"),
    CANCEL_AUTO_PUSH_COIN(25,"取消自动投币"),
    PILE_TOWER(26,"炼金塔-堆塔"),
    ;

    private int code;
    private String name;

    ProductOperationEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**
     * 根据code获取名称
     */
    public static String loadByCode(int code){
        ProductOperationEnum[] msgArr = ProductOperationEnum.values();
        for(ProductOperationEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
