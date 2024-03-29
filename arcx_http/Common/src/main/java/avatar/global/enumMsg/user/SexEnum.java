package avatar.global.enumMsg.user;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别数据字典
 */
public enum SexEnum {
    MALE(1,"男"),
    FEMALE(2,"女"),
    ;

    private int code;
    private String name;

    SexEnum(int code, String name){
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
        for (SexEnum enumMsg : SexEnum.values()) {
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
