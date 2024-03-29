package avatar.global.enumMsg.basic.system;

import java.util.*;

/**
 * 手机平台类型
 */
public enum MobilePlatformTypeEnum {
    APPLE(1,"苹果"),
    ANDROID(2,"安卓"),
    WEB(3,"web"),
    ;

    private int code;
    private String name;

    MobilePlatformTypeEnum(int code, String name){
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
    public static List<MobilePlatformTypeEnum> loadAll(){
        MobilePlatformTypeEnum[] enumArr = MobilePlatformTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (MobilePlatformTypeEnum enumMsg : MobilePlatformTypeEnum.values()) {
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
