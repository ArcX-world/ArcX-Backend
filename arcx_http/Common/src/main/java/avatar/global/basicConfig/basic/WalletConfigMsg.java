package avatar.global.basicConfig.basic;

/**
 * nft配置信息
 */
public class WalletConfigMsg {
    //最小SOL提取数量
    public static final double minSolNum = 0.01;
    //最小USDT提取数量
    public static final double minUsdtNum = 0.1;
    //最小AXC提取数量
    public static final int minAxcNum = 1;
    //最小ARCX提取数量
    public static final int minArcxNum = 1;
    //SOL提取上限
    public static final int maxSolNum = 10000;
    //USDT提取上限
    public static final int maxUsdtNum = 10000;
    //AXC提取上限
    public static final int maxAxcNum = 10000;
    //ARCX提取上限
    public static final int maxArcxNum = 10000;
    //SOL提取费用
    public static final double solFee = 0.001;
    //USDT提取费用
    public static final double usdtFee = 0.5;
    //AXC提取费用
    public static final int axcFee = 100;
    //ARCX提取费用
    public static final int arcxFee = 10;
    //外部费用
    public static final String extraFee = "Solana Gas";

}
