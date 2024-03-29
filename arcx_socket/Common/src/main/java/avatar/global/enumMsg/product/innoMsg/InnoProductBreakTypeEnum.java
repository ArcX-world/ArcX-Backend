package avatar.global.enumMsg.product.innoMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备故障类型数据字典
 */
public enum InnoProductBreakTypeEnum {
    WIN_COIN_ON_COIN_ERROR(1,"中奖上币故障"),
    GET_COIN_ERROR(2,"获得币回币故障"),
    EARN_COIN_ERROR(3,"退币回币故障"),
    PUSH_PLATE_ERROR(4,"推盘故障"),
    WIPER_ERROR(5,"雨刷故障"),
    DRAGON_BALL_ERROR(6,"龙珠故障"),
    PUSH_COIN_ERROR(7,"投币故障"),
    ;

    private int code;
    private String name;

    InnoProductBreakTypeEnum(int code, String name){
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
        for (InnoProductBreakTypeEnum breakTypeEnum : InnoProductBreakTypeEnum.values()) {
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
