package avatar.global.enumMsg.basic.nft;

import java.util.*;

/**
 * 枚举：NFT属性类型
 */
public enum NftAttributeTypeEnum {
    LV(1,"等级"),
    SPACE(2,"储币空间"),
    INCOME(3,"进货折扣"),
    ;

    private int code;
    private String name;//中文名称

    NftAttributeTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode(){
        return code;
    }

    public String getName(){return name;}

    /**
     * 获取所有枚举
     */
    public static List<NftAttributeTypeEnum> loadAll(){
        NftAttributeTypeEnum[] enumArr = NftAttributeTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NftAttributeTypeEnum pay : NftAttributeTypeEnum.values()) {
            map.put(pay.getCode(), pay.getName());
        }
        return map;
    }

    /**
     * 根据类型获取名称
     */
    public static String getNameByCode(int code){
        return toMap().get(code);
    }

}
