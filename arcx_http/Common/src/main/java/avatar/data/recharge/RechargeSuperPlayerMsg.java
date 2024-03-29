package avatar.data.recharge;

/**
 * 超级玩家充值信息
 */
public class RechargeSuperPlayerMsg {
    //USDT价格
    private int usdtAmt;

    //图片
    private String pct;

    public int getUsdtAmt() {
        return usdtAmt;
    }

    public void setUsdtAmt(int usdtAmt) {
        this.usdtAmt = usdtAmt;
    }

    public String getPct() {
        return pct;
    }

    public void setPct(String pct) {
        this.pct = pct;
    }
}
