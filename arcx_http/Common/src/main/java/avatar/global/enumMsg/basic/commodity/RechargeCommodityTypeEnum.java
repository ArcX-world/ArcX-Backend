package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**
 * 充值商品类型
 */
public enum RechargeCommodityTypeEnum {
    SUPER_PLAYER(1,"超级玩家"),
    GOLD(2, "金币"),
    PROPERTY(3,"道具"),
    ;

    private int code;
    private String name;

    RechargeCommodityTypeEnum(int code, String name){
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
    public static List<RechargeCommodityTypeEnum> loadAll(){
        RechargeCommodityTypeEnum[] enumArr = RechargeCommodityTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (RechargeCommodityTypeEnum awardTypeEnum : RechargeCommodityTypeEnum.values()) {
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
