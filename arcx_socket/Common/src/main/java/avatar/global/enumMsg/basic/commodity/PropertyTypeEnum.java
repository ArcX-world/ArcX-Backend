package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**
 * 商品类型
 */
public enum PropertyTypeEnum {
    COFFEE(1,"咖啡"),
    ENERGY_DRINK(2, "能量饮料"),
    PEPE_STICKER(3, "Pepe贴纸"),
    DOGE_STICKER(4, "DOGE贴纸"),
    SHIB_STICKER(5, "SHIB贴纸"),
    WIF_STICKER(6, "WIF贴纸"),
    SILLY_STICKER(7, "Silly贴纸"),
    TESLA(8, "特斯拉"),
    SNATCH_CARD(9, "抢夺卡"),
    HOMEOWNER_CARD(10, "房主卡"),
    EQUALIZATION_CARD(11, "均富卡"),
    ;

    private int code;
    private String name;

    PropertyTypeEnum(int code, String name){
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
    public static List<PropertyTypeEnum> loadAll(){
        PropertyTypeEnum[] enumArr = PropertyTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PropertyTypeEnum awardTypeEnum : PropertyTypeEnum.values()) {
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
