package avatar.global.enumMsg.user;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家状态数据字典
 */
public enum UserStatusEnum {
    NORMAL(0,"正常"),
    FORBID(1,"禁用"),
    LOGOUT(2,"注销"),
    ;

    private int code;
    private String name;

    UserStatusEnum(int code, String name){
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
        for (UserStatusEnum enumMsg : UserStatusEnum.values()) {
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
