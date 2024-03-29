package avatar.global.enumMsg.product.award;

import java.util.HashMap;
import java.util.Map;

/**
 * 自研设备中奖类型数据字典
 */
public enum ProductAwardTypeEnum {
    FREE_GAME(1,"免费游戏"),
    GRM(2,"宝石游戏"),
    PRIZE_WHEEL_GRAND(3,"大奖转盘-grand奖池"),
    PRIZE_WHEEL_MAJOR(4,"大奖转盘-major奖池"),
    PRIZE_WHEEL_MINOR(5,"大奖转盘-minor奖池"),
    DRAGON_BALL(6,"龙珠"),
    AVERAGE_AWARD(7,"普通奖励"),
    AGYPT_OPEN_BOX(8,"埃及开箱子"),
    GOSSIP(9,"八卦"),
    COPPER_FULL(10,"铜钱集满"),
    HERO_BATTLE(11,"三国战斗"),
    THUNDER(12,"闪电"),
    THUNDER_UP(13,"闪电UP"),
    THUNDER_PRIZE_MINOR(14,"闪电minor彩金"),
    THUNDER_PRIZE_MAJOR(15,"闪电major彩金"),
    THUNDER_PRIZE_GRAND(16,"闪电grand彩金"),
    THUNDER_COLLECT(17,"闪电收集"),
    WHISTLE(18,"口哨"),
    PIRATE_CANNON(19,"海盗开炮"),
    ;

    private int code;
    private String name;

    ProductAwardTypeEnum(int code, String name){
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
        for (ProductAwardTypeEnum awardTypeEnum : ProductAwardTypeEnum.values()) {
            map.put(awardTypeEnum.getCode(), awardTypeEnum.getName());
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
