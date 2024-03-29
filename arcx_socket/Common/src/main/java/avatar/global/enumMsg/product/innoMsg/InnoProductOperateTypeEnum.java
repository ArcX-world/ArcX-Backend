package avatar.global.enumMsg.product.innoMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * 自研设备操作类型数据字典
 */
public enum InnoProductOperateTypeEnum {
    PUSH_COIN(1,"投币"),
    GET_COIN(2,"获得币"),
    WIN_PRIZE(3,"中奖"),
    BREAK_DOWN(4,"故障"),
    WIPER(5,"雨刷"),
    AUTO_PUSH_COIN(6,"自动投币"),
    CANCEL_AUTO_PUSH_COIN(7,"取消自动投币"),
    ;

    private int code;
    private String name;

    InnoProductOperateTypeEnum(int code, String name){
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
        for (InnoProductOperateTypeEnum breakTypeEnum : InnoProductOperateTypeEnum.values()) {
            map.put(breakTypeEnum.getCode(), breakTypeEnum.getName());
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
