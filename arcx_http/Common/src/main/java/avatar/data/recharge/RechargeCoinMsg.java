package avatar.data.recharge;

/**
 * 充值金币信息
 */
public class RechargeCoinMsg {
    //商品ID
    private int cmdId;

    //游戏币数量
    private long cnAmt;

    //USDT价格
    private int usdtAmt;

    //图片
    private String pct;

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public long getCnAmt() {
        return cnAmt;
    }

    public void setCnAmt(long cnAmt) {
        this.cnAmt = cnAmt;
    }

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
