package avatar.global.enumMsg.product;

/**
 * 设备类型
 */
public enum ProductTypeEnum {
    PUSH_COIN_MACHINE(1,"推币机"),
    DOLL_MACHINE(2,"娃娃机"),
    PRESENT_MACHINE(3,"礼品机"),
    ;

    private int code;
    private String name;

    ProductTypeEnum(int code, String name){
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
     * 根据code获取名称
     */
    public static String loadNameByCode(int code){
        ProductTypeEnum[] msgArr = ProductTypeEnum.values();
        for(ProductTypeEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
