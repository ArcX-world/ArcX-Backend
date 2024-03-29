package avatar.global.enumMsg.product.award;

import java.util.*;

/**
 * 普通设备奖励
 */
public enum NormalProductAwardTypeEnum {
    PILE_TOWER(1,"炼金塔堆塔"),;

    private int code;
    private String name;

    NormalProductAwardTypeEnum(int code, String name){
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
    public static List<NormalProductAwardTypeEnum> loadAll(){
        NormalProductAwardTypeEnum[] enumArr = NormalProductAwardTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NormalProductAwardTypeEnum enumMsg : NormalProductAwardTypeEnum.values()) {
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
