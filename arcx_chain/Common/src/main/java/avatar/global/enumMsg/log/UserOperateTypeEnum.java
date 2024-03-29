package avatar.global.enumMsg.log;

import java.util.*;

/**
 * 玩家操作类型枚举
 */
public enum UserOperateTypeEnum {
    REGISTER(1,"注册"),
    COST_GOLG_COIN(2,"消费金币"),
    START_GAME(3,"开始游戏"),
    SETTLEMENT_GAME(4,"结算游戏"),
    LOGOUT_ACCOUNT(5, "注销账号"),
    REPAIR_PRODUCT(6,"报修机器"),
    JOIN_PRODUCT(7,"进入设备"),
    EXIT_PRODUCT(8,"退出设备"),
    SIGN(9,"签到"),
    COST_PROPERTY(10,"消费道具"),
    COST_AXC(11,"消费AXC"),
    COST_USDT(12,"消费USDT"),
    ;

    private int code;
    private String name;

    UserOperateTypeEnum(int code, String name){
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
    public static List<UserOperateTypeEnum> loadAll(){
        UserOperateTypeEnum[] enumArr = UserOperateTypeEnum.values();
        return new ArrayList<>(Arrays.asList(enumArr));
    }

    /**
     * 转换成对象
     */
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (UserOperateTypeEnum enumMsg : UserOperateTypeEnum.values()) {
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
