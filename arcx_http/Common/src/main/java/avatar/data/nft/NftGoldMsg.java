package avatar.data.nft;

/**
 * NFT售币机储币信息
 */
public class NftGoldMsg {
    //当前储币值
    private long cnGdAmt;

    //可添加的储币值
    private long upGdAmt;

    //折扣(%)
    private double dscot;

    //未打折前的金币数量
    private long oriAmt;

    //营业税(%)
    private int tax;

    public long getCnGdAmt() {
        return cnGdAmt;
    }

    public void setCnGdAmt(long cnGdAmt) {
        this.cnGdAmt = cnGdAmt;
    }

    public long getUpGdAmt() {
        return upGdAmt;
    }

    public void setUpGdAmt(long upGdAmt) {
        this.upGdAmt = upGdAmt;
    }

    public double getDscot() {
        return dscot;
    }

    public void setDscot(double dscot) {
        this.dscot = dscot;
    }

    public long getOriAmt() {
        return oriAmt;
    }

    public void setOriAmt(long oriAmt) {
        this.oriAmt = oriAmt;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
