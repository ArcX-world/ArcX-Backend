package avatar.data.nft;

/**
 * NFT售币机耐久度信息
 */
public class NftDurabilityMsg {
    //耐久度
    private long durbtyAmt;

    //商品类型
    private int cmdTp;

    //商品数量
    private long csAmt;

    public long getDurbtyAmt() {
        return durbtyAmt;
    }

    public void setDurbtyAmt(long durbtyAmt) {
        this.durbtyAmt = durbtyAmt;
    }

    public int getCmdTp() {
        return cmdTp;
    }

    public void setCmdTp(int cmdTp) {
        this.cmdTp = cmdTp;
    }

    public long getCsAmt() {
        return csAmt;
    }

    public void setCsAmt(long csAmt) {
        this.csAmt = csAmt;
    }
}
