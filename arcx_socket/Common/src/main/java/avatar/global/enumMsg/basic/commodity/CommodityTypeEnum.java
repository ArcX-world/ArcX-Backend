package avatar.global.enumMsg.basic.commodity;

import java.util.*;

/**
 * 商品类型
 */
public enum CommodityTypeEnum {
    NONE(0,"无"),
    GOLD_COIN(1, "金币"),
    AXC(2,"AXC"),
    EXP(3,"经验"),
    PROPERTY(4,"道具"),
    AGAIN(5,"再来一次"),
    SOLANA(6,"SOL"),
    USDT(7,"UDST"),
    NFT_DEBRIS(8,"NFT碎片"),
    ;

    private int code;
    private String name;

    CommodityTypeEnum(int code, String name){
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
    public static List<CommodityTypeEnum> loadAll(){
        CommodityTypeEnum[] enumArr = CommodityTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (CommodityTypeEnum awardTypeEnum : CommodityTypeEnum.values()) {
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
