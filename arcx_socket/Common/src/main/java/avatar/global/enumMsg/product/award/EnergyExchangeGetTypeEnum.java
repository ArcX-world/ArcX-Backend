package avatar.global.enumMsg.product.award;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 能量兑换获得类型
 */
public enum EnergyExchangeGetTypeEnum {
    DRAGON_BALL(1,"龙珠"),
    DOLL_MACHINE(2,"娃娃机"),
    PRESENT_MACHINE(3,"礼品机"),
    ;

    private int code;
    private String name;

    EnergyExchangeGetTypeEnum(int code, String name){
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
    public static List<EnergyExchangeGetTypeEnum> loadAll(){
        EnergyExchangeGetTypeEnum[] enumArr = EnergyExchangeGetTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }
    
    /**
     * 根据code获取名称
     */
    public static String loadNameByCode(int code){
        EnergyExchangeGetTypeEnum[] msgArr = EnergyExchangeGetTypeEnum.values();
        for(EnergyExchangeGetTypeEnum enumMsg : msgArr){
            if(enumMsg.code == code){
                return enumMsg.getName();
            }
        }
        return null;
    }
}
