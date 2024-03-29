package avatar.global.enumMsg.basic.system;

import java.util.*;

/**
 * 登录方式
 */
public enum LoginWayTypeEnum {
    TOURIST(1, "游客登录"),
    APPLE(2,"苹果登录"),
    EMAIL(3,"邮箱登录"),
    ;

    private int code;
    private String name;

    LoginWayTypeEnum(int code, String name){
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
    public static List<LoginWayTypeEnum> loadAll(){
        LoginWayTypeEnum[] enumArr = LoginWayTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }
    
    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (LoginWayTypeEnum enumMsg : LoginWayTypeEnum.values()) {
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
