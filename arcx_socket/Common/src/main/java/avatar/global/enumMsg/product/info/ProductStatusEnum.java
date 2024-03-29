package avatar.global.enumMsg.product.info;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备状态数据字典
 */
public enum ProductStatusEnum {
    NORMAL(0,"正常"),
    REPAIR(1,"维护"),
    SOLD_OUT(2,"下架"),
    ;

    private int code;
    private String name;

    ProductStatusEnum(int code, String name){
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
        for (ProductStatusEnum enumMsg : ProductStatusEnum.values()) {
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
