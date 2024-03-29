package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：代币类型
 */
public enum TokensTypeEnum {
    SOLANA(1,"solana"),
    AXC(2,"AXC"),
    USDT(3,"USDT"),
    ;

    private int code;
    private String name;

    TokensTypeEnum(int code, String name){
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
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TokensTypeEnum enumMsg : TokensTypeEnum.values()) {
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
