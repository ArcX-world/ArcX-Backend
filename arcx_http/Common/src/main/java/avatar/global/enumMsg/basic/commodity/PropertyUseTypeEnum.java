package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**
 * 道具使用类型
 */
public enum PropertyUseTypeEnum {
    RESTORE_ENERGY(1,"恢复能量"),
    ;

    private int code;
    private String name;

    PropertyUseTypeEnum(int code, String name){
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
    public static List<PropertyUseTypeEnum> loadAll(){
        PropertyUseTypeEnum[] enumArr = PropertyUseTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (PropertyUseTypeEnum awardTypeEnum : PropertyUseTypeEnum.values()) {
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
