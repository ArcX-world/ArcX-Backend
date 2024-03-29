package avatar.global.enumMsg.system;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举：是否
 */
public enum YesOrNoEnum {
    NO(0,"否"),
    YES(1,"是"),
    ;

    private int code;
    private String name;

    YesOrNoEnum(int code, String name){
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
        for (YesOrNoEnum enumMsg : YesOrNoEnum.values()) {
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
