package avatar.global.code.basicConfig;

/**
 * 设备配置信息
 */
public class ProductConfigMsg {
    //最大投币倍率等级
    public static final int maxCoinMultiLevel = 4;
    //自研奖项保存时间
    public static final long innoAwardTillTime = 120000;
    //自研设备中奖刷新时间（毫秒）
    public static final int innoProductAwardRefreshTime = 20000;
    //自研NA付费检测时间
    public static final long inoNaPayCheckTime = 600000;
    //埃及开箱子持续时间
    public static final long agyptOpenBoxTillTime = 360;
    //小丑套圈持续时间
    public static final long clownCircusFerruleTillTime = 550;
    //海盗开炮持续时间
    public static final long pirateCannonTillTime = 180;
    //推币机获得币时间
    public static int getCoinTime=1000;
    //推币机开始游戏获得币时间
    public static int startGameGetCoinTime = 6;
    //自研无中奖流程类型
    public static final String innoNoProcessAward = "10,17";
    //自研只记录流程类型
    public static final String innoJustDataAward = "13,14,15,16";
    //自研设备特殊中奖处理类型
    public static final String innoSpecialAwardDeal = "8,18,19";
    //公众号token超时时间
    public static final int OFFICAL_TOKEN_OUT_TIME = 7000;
    //彩票游戏币兑比
    public static final int lotteryCoinExchange = 100;
    //推塔连续次数
    public static final int pileTillTime = 3;
    //堆塔停止时间
    public static final long pileStopTime = 7000;
    //复位次数
    public static int resetTime=10;
}
