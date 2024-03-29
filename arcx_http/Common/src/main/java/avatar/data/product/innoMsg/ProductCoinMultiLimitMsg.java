package avatar.data.product.innoMsg;

/**
 * 设备倍率限制信息
 */
public class ProductCoinMultiLimitMsg {
    //倍率
    private int mulAmt;

    //是否限制
    private int lmFlg;

    public int getMulAmt() {
        return mulAmt;
    }

    public void setMulAmt(int mulAmt) {
        this.mulAmt = mulAmt;
    }

    public int getLmFlg() {
        return lmFlg;
    }

    public void setLmFlg(int lmFlg) {
        this.lmFlg = lmFlg;
    }
}
