package avatar.global.enumMsg.user;

import java.util.*;

/**
 * 玩家属性类型数据字典
 */
public enum UserAttributeTypeEnum {
    EXP_LEVEL(1,"经验等级"),
    ENERGY_LEVEL(2,"能量等级"),
    CHARGING_LEVEL(3,"充能等级"),
    AIRDROP_LEVEL(4,"空投等级"),
    LUCKY_LEVEL(5,"幸运等级"),
    CHARM_LEVEL(6,"魅力等级"),

    ;

    private int code;
    private String name;

    UserAttributeTypeEnum(int code, String name){
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
    public static List<UserAttributeTypeEnum> loadAll(){
        UserAttributeTypeEnum[] enumArr = UserAttributeTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (UserAttributeTypeEnum enumMsg : UserAttributeTypeEnum.values()) {
            map.put(enumMsg.getCode(), enumMsg.getName());
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
