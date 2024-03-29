package avatar.global.enumMsg.basic.nft;

import java.util.*;

/**
 * 枚举：NFT类型
 */
public enum NftTypeEnum {
    SELL_COIN_MACHINE(1,"售币机"),
    PUSH_COIN(2,"推币机"),
    CATCH_DOLL(3,"娃娃机"),
    PRESENT(4,"礼品机"),
    ;

    private int code;
    private String name;//中文名称

    NftTypeEnum(int code, String name){
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
    public static List<NftTypeEnum> loadAll(){
        NftTypeEnum[] enumArr = NftTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (NftTypeEnum pay : NftTypeEnum.values()) {
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
