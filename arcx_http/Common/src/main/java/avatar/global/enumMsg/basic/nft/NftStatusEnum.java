package avatar.global.enumMsg.basic.nft;

import java.util.*;

/**
 * 枚举：NFT状态
 */
public enum NftStatusEnum {
    UNUSED(1,"闲置"),
    LISTING(2,"上架状态"),
    ADVERTISING(3,"广告中"),
    IN_OPERATION(4,"营业中"),
    ;

    private int code;
    private String name;//中文名称

    NftStatusEnum(int code, String name){
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
    public static List<NftStatusEnum> loadAll(){
        NftStatusEnum[] enumArr = NftStatusEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NftStatusEnum pay : NftStatusEnum.values()) {
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
