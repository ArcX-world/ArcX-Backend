package avatar.data.nft;

/**
 * NFT报告信息
 */
public class NftReportMsg {
    //出售时间
    private long opTm;

    //出售价格
    private long opPrc;

    //实际收入
    private double inc;

    //剩余金币数
    private long blAmt;

    //费率
    private int tax;

    public long getOpTm() {
        return opTm;
    }

    public void setOpTm(long opTm) {
        this.opTm = opTm;
    }

    public long getOpPrc() {
        return opPrc;
    }

    public void setOpPrc(long opPrc) {
        this.opPrc = opPrc;
    }

    public double getInc() {
        return inc;
    }

    public void setInc(double inc) {
        this.inc = inc;
    }

    public long getBlAmt() {
        return blAmt;
    }

    public void setBlAmt(long blAmt) {
        this.blAmt = blAmt;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
