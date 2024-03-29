package avatar.global.enumMsg.product.info;

/**
 * 设备二级类型
 */
public enum ProductSecondTypeEnum {
    KING_KONG(1,"自研金刚", "kingkong"),
    AGYPT(2,"自研埃及", "agypt"),
    EAST(3,"自研东方魔力", "east"),
    POWER_THUNDER(4,"自研雷电之力","thunder"),
    CLOWN_CIRCUS(5,"小丑马戏团","clownCircus"),
    PIRATE(6,"海盗","pirate"),
    PILE_TOWER(7,"炼金塔","pileTower"),
    ;

    private int code;
    private String name;
    private String enName;

    ProductSecondTypeEnum(int code, String name, String enName){
        this.code = code;
        this.name = name;
        this.enName = enName;
    }

    public int getCode(){
        return code;
    }

    public String getName(){
        return name;
    }

    public String getEnName() {
        return enName;
    }

}
